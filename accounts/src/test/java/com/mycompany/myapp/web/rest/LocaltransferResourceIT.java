package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LocaltransferAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BankAccount;
import com.mycompany.myapp.domain.Localtransfer;
import com.mycompany.myapp.repository.LocaltransferRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocaltransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocaltransferResourceIT {

    private static final String DEFAULT_SENDER_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_BANK_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_BANK_BRANCH = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/localtransfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocaltransferRepository localtransferRepository;

    @Mock
    private LocaltransferRepository localtransferRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocaltransferMockMvc;

    private Localtransfer localtransfer;

    private Localtransfer insertedLocaltransfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localtransfer createEntity(EntityManager em) {
        Localtransfer localtransfer = new Localtransfer()
            .senderAccountNumber(DEFAULT_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(DEFAULT_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(DEFAULT_RECIPIENT_BANK_NAME)
            .recipientBankBranch(DEFAULT_RECIPIENT_BANK_BRANCH)
            .amount(DEFAULT_AMOUNT)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createEntity();
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        localtransfer.setBankAccount(bankAccount);
        return localtransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localtransfer createUpdatedEntity(EntityManager em) {
        Localtransfer updatedLocaltransfer = new Localtransfer()
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(UPDATED_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createUpdatedEntity();
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        updatedLocaltransfer.setBankAccount(bankAccount);
        return updatedLocaltransfer;
    }

    @BeforeEach
    public void initTest() {
        localtransfer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocaltransfer != null) {
            localtransferRepository.delete(insertedLocaltransfer);
            insertedLocaltransfer = null;
        }
    }

    @Test
    @Transactional
    void createLocaltransfer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Localtransfer
        var returnedLocaltransfer = om.readValue(
            restLocaltransferMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Localtransfer.class
        );

        // Validate the Localtransfer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocaltransferUpdatableFieldsEquals(returnedLocaltransfer, getPersistedLocaltransfer(returnedLocaltransfer));

        insertedLocaltransfer = returnedLocaltransfer;
    }

    @Test
    @Transactional
    void createLocaltransferWithExistingId() throws Exception {
        // Create the Localtransfer with an existing ID
        localtransfer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocaltransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isBadRequest());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSenderAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localtransfer.setSenderAccountNumber(null);

        // Create the Localtransfer, which fails.

        restLocaltransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localtransfer.setRecipientAccountNumber(null);

        // Create the Localtransfer, which fails.

        restLocaltransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientBankNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localtransfer.setRecipientBankName(null);

        // Create the Localtransfer, which fails.

        restLocaltransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localtransfer.setAmount(null);

        // Create the Localtransfer, which fails.

        restLocaltransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localtransfer.setTransactionDate(null);

        // Create the Localtransfer, which fails.

        restLocaltransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocaltransfers() throws Exception {
        // Initialize the database
        insertedLocaltransfer = localtransferRepository.saveAndFlush(localtransfer);

        // Get all the localtransferList
        restLocaltransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localtransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].senderAccountNumber").value(hasItem(DEFAULT_SENDER_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].recipientAccountNumber").value(hasItem(DEFAULT_RECIPIENT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].recipientBankName").value(hasItem(DEFAULT_RECIPIENT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].recipientBankBranch").value(hasItem(DEFAULT_RECIPIENT_BANK_BRANCH)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocaltransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(localtransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocaltransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localtransferRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocaltransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(localtransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocaltransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(localtransferRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLocaltransfer() throws Exception {
        // Initialize the database
        insertedLocaltransfer = localtransferRepository.saveAndFlush(localtransfer);

        // Get the localtransfer
        restLocaltransferMockMvc
            .perform(get(ENTITY_API_URL_ID, localtransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localtransfer.getId().intValue()))
            .andExpect(jsonPath("$.senderAccountNumber").value(DEFAULT_SENDER_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.recipientAccountNumber").value(DEFAULT_RECIPIENT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.recipientBankName").value(DEFAULT_RECIPIENT_BANK_NAME))
            .andExpect(jsonPath("$.recipientBankBranch").value(DEFAULT_RECIPIENT_BANK_BRANCH))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingLocaltransfer() throws Exception {
        // Get the localtransfer
        restLocaltransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocaltransfer() throws Exception {
        // Initialize the database
        insertedLocaltransfer = localtransferRepository.saveAndFlush(localtransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localtransfer
        Localtransfer updatedLocaltransfer = localtransferRepository.findById(localtransfer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocaltransfer are not directly saved in db
        em.detach(updatedLocaltransfer);
        updatedLocaltransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(UPDATED_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restLocaltransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocaltransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocaltransfer))
            )
            .andExpect(status().isOk());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocaltransferToMatchAllProperties(updatedLocaltransfer);
    }

    @Test
    @Transactional
    void putNonExistingLocaltransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localtransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaltransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localtransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localtransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocaltransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localtransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaltransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localtransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocaltransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localtransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaltransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocaltransferWithPatch() throws Exception {
        // Initialize the database
        insertedLocaltransfer = localtransferRepository.saveAndFlush(localtransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localtransfer using partial update
        Localtransfer partialUpdatedLocaltransfer = new Localtransfer();
        partialUpdatedLocaltransfer.setId(localtransfer.getId());

        partialUpdatedLocaltransfer
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE);

        restLocaltransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocaltransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocaltransfer))
            )
            .andExpect(status().isOk());

        // Validate the Localtransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocaltransferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocaltransfer, localtransfer),
            getPersistedLocaltransfer(localtransfer)
        );
    }

    @Test
    @Transactional
    void fullUpdateLocaltransferWithPatch() throws Exception {
        // Initialize the database
        insertedLocaltransfer = localtransferRepository.saveAndFlush(localtransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localtransfer using partial update
        Localtransfer partialUpdatedLocaltransfer = new Localtransfer();
        partialUpdatedLocaltransfer.setId(localtransfer.getId());

        partialUpdatedLocaltransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(UPDATED_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restLocaltransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocaltransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocaltransfer))
            )
            .andExpect(status().isOk());

        // Validate the Localtransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocaltransferUpdatableFieldsEquals(partialUpdatedLocaltransfer, getPersistedLocaltransfer(partialUpdatedLocaltransfer));
    }

    @Test
    @Transactional
    void patchNonExistingLocaltransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localtransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaltransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localtransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(localtransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocaltransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localtransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaltransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(localtransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocaltransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localtransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaltransferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(localtransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localtransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocaltransfer() throws Exception {
        // Initialize the database
        insertedLocaltransfer = localtransferRepository.saveAndFlush(localtransfer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the localtransfer
        restLocaltransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, localtransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return localtransferRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Localtransfer getPersistedLocaltransfer(Localtransfer localtransfer) {
        return localtransferRepository.findById(localtransfer.getId()).orElseThrow();
    }

    protected void assertPersistedLocaltransferToMatchAllProperties(Localtransfer expectedLocaltransfer) {
        assertLocaltransferAllPropertiesEquals(expectedLocaltransfer, getPersistedLocaltransfer(expectedLocaltransfer));
    }

    protected void assertPersistedLocaltransferToMatchUpdatableProperties(Localtransfer expectedLocaltransfer) {
        assertLocaltransferAllUpdatablePropertiesEquals(expectedLocaltransfer, getPersistedLocaltransfer(expectedLocaltransfer));
    }
}
