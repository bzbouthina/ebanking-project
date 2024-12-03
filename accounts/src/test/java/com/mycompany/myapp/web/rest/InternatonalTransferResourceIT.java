package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.InternatonalTransferAsserts.*;
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
import com.mycompany.myapp.domain.InternatonalTransfer;
import com.mycompany.myapp.domain.enumeration.CurrencyType;
import com.mycompany.myapp.repository.InternatonalTransferRepository;
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
 * Integration tests for the {@link InternatonalTransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InternatonalTransferResourceIT {

    private static final String DEFAULT_SENDER_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_IBAN = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_IBAN = "BBBBBBBBBBBBBBB";

    private static final String DEFAULT_SWIFT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SWIFT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final CurrencyType DEFAULT_CURRENCY = CurrencyType.USD;
    private static final CurrencyType UPDATED_CURRENCY = CurrencyType.EUR;

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/internatonal-transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InternatonalTransferRepository internatonalTransferRepository;

    @Mock
    private InternatonalTransferRepository internatonalTransferRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternatonalTransferMockMvc;

    private InternatonalTransfer internatonalTransfer;

    private InternatonalTransfer insertedInternatonalTransfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternatonalTransfer createEntity(EntityManager em) {
        InternatonalTransfer internatonalTransfer = new InternatonalTransfer()
            .senderAccountNumber(DEFAULT_SENDER_ACCOUNT_NUMBER)
            .recipientIban(DEFAULT_RECIPIENT_IBAN)
            .swiftCode(DEFAULT_SWIFT_CODE)
            .recipientName(DEFAULT_RECIPIENT_NAME)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
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
        internatonalTransfer.setBankAccount(bankAccount);
        return internatonalTransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternatonalTransfer createUpdatedEntity(EntityManager em) {
        InternatonalTransfer updatedInternatonalTransfer = new InternatonalTransfer()
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
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
        updatedInternatonalTransfer.setBankAccount(bankAccount);
        return updatedInternatonalTransfer;
    }

    @BeforeEach
    public void initTest() {
        internatonalTransfer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedInternatonalTransfer != null) {
            internatonalTransferRepository.delete(insertedInternatonalTransfer);
            insertedInternatonalTransfer = null;
        }
    }

    @Test
    @Transactional
    void createInternatonalTransfer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InternatonalTransfer
        var returnedInternatonalTransfer = om.readValue(
            restInternatonalTransferMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InternatonalTransfer.class
        );

        // Validate the InternatonalTransfer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInternatonalTransferUpdatableFieldsEquals(
            returnedInternatonalTransfer,
            getPersistedInternatonalTransfer(returnedInternatonalTransfer)
        );

        insertedInternatonalTransfer = returnedInternatonalTransfer;
    }

    @Test
    @Transactional
    void createInternatonalTransferWithExistingId() throws Exception {
        // Create the InternatonalTransfer with an existing ID
        internatonalTransfer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSenderAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setSenderAccountNumber(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientIbanIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setRecipientIban(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSwiftCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setSwiftCode(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setRecipientName(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setAmount(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setCurrency(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internatonalTransfer.setTransactionDate(null);

        // Create the InternatonalTransfer, which fails.

        restInternatonalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInternatonalTransfers() throws Exception {
        // Initialize the database
        insertedInternatonalTransfer = internatonalTransferRepository.saveAndFlush(internatonalTransfer);

        // Get all the internatonalTransferList
        restInternatonalTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internatonalTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].senderAccountNumber").value(hasItem(DEFAULT_SENDER_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].recipientIban").value(hasItem(DEFAULT_RECIPIENT_IBAN)))
            .andExpect(jsonPath("$.[*].swiftCode").value(hasItem(DEFAULT_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].recipientName").value(hasItem(DEFAULT_RECIPIENT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInternatonalTransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(internatonalTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInternatonalTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(internatonalTransferRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInternatonalTransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(internatonalTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInternatonalTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(internatonalTransferRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInternatonalTransfer() throws Exception {
        // Initialize the database
        insertedInternatonalTransfer = internatonalTransferRepository.saveAndFlush(internatonalTransfer);

        // Get the internatonalTransfer
        restInternatonalTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, internatonalTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(internatonalTransfer.getId().intValue()))
            .andExpect(jsonPath("$.senderAccountNumber").value(DEFAULT_SENDER_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.recipientIban").value(DEFAULT_RECIPIENT_IBAN))
            .andExpect(jsonPath("$.swiftCode").value(DEFAULT_SWIFT_CODE))
            .andExpect(jsonPath("$.recipientName").value(DEFAULT_RECIPIENT_NAME))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingInternatonalTransfer() throws Exception {
        // Get the internatonalTransfer
        restInternatonalTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInternatonalTransfer() throws Exception {
        // Initialize the database
        insertedInternatonalTransfer = internatonalTransferRepository.saveAndFlush(internatonalTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the internatonalTransfer
        InternatonalTransfer updatedInternatonalTransfer = internatonalTransferRepository
            .findById(internatonalTransfer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInternatonalTransfer are not directly saved in db
        em.detach(updatedInternatonalTransfer);
        updatedInternatonalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restInternatonalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInternatonalTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInternatonalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInternatonalTransferToMatchAllProperties(updatedInternatonalTransfer);
    }

    @Test
    @Transactional
    void putNonExistingInternatonalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internatonalTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternatonalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internatonalTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(internatonalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInternatonalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internatonalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternatonalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(internatonalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInternatonalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internatonalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternatonalTransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInternatonalTransferWithPatch() throws Exception {
        // Initialize the database
        insertedInternatonalTransfer = internatonalTransferRepository.saveAndFlush(internatonalTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the internatonalTransfer using partial update
        InternatonalTransfer partialUpdatedInternatonalTransfer = new InternatonalTransfer();
        partialUpdatedInternatonalTransfer.setId(internatonalTransfer.getId());

        partialUpdatedInternatonalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restInternatonalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternatonalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInternatonalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InternatonalTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInternatonalTransferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInternatonalTransfer, internatonalTransfer),
            getPersistedInternatonalTransfer(internatonalTransfer)
        );
    }

    @Test
    @Transactional
    void fullUpdateInternatonalTransferWithPatch() throws Exception {
        // Initialize the database
        insertedInternatonalTransfer = internatonalTransferRepository.saveAndFlush(internatonalTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the internatonalTransfer using partial update
        InternatonalTransfer partialUpdatedInternatonalTransfer = new InternatonalTransfer();
        partialUpdatedInternatonalTransfer.setId(internatonalTransfer.getId());

        partialUpdatedInternatonalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restInternatonalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternatonalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInternatonalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InternatonalTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInternatonalTransferUpdatableFieldsEquals(
            partialUpdatedInternatonalTransfer,
            getPersistedInternatonalTransfer(partialUpdatedInternatonalTransfer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInternatonalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internatonalTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternatonalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, internatonalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(internatonalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInternatonalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internatonalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternatonalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(internatonalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInternatonalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internatonalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternatonalTransferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(internatonalTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternatonalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInternatonalTransfer() throws Exception {
        // Initialize the database
        insertedInternatonalTransfer = internatonalTransferRepository.saveAndFlush(internatonalTransfer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the internatonalTransfer
        restInternatonalTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, internatonalTransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return internatonalTransferRepository.count();
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

    protected InternatonalTransfer getPersistedInternatonalTransfer(InternatonalTransfer internatonalTransfer) {
        return internatonalTransferRepository.findById(internatonalTransfer.getId()).orElseThrow();
    }

    protected void assertPersistedInternatonalTransferToMatchAllProperties(InternatonalTransfer expectedInternatonalTransfer) {
        assertInternatonalTransferAllPropertiesEquals(
            expectedInternatonalTransfer,
            getPersistedInternatonalTransfer(expectedInternatonalTransfer)
        );
    }

    protected void assertPersistedInternatonalTransferToMatchUpdatableProperties(InternatonalTransfer expectedInternatonalTransfer) {
        assertInternatonalTransferAllUpdatablePropertiesEquals(
            expectedInternatonalTransfer,
            getPersistedInternatonalTransfer(expectedInternatonalTransfer)
        );
    }
}
