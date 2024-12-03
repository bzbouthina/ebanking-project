package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CardPaymentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CardPayment;
import com.mycompany.myapp.domain.Payment;
import com.mycompany.myapp.repository.CardPaymentRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CardPaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardPaymentResourceIT {

    private static final String DEFAULT_CARD_NUMBER = "0380232715492117";
    private static final String UPDATED_CARD_NUMBER = "4841516280966184";

    private static final LocalDate DEFAULT_CARD_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CARD_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CARD_HOLDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CARD_HOLDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CVV = "222";
    private static final String UPDATED_CVV = "901";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CardPaymentRepository cardPaymentRepository;

    @Mock
    private CardPaymentRepository cardPaymentRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardPaymentMockMvc;

    private CardPayment cardPayment;

    private CardPayment insertedCardPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardPayment createEntity(EntityManager em) {
        CardPayment cardPayment = new CardPayment()
            .cardNumber(DEFAULT_CARD_NUMBER)
            .cardExpiryDate(DEFAULT_CARD_EXPIRY_DATE)
            .cardHolderName(DEFAULT_CARD_HOLDER_NAME)
            .cvv(DEFAULT_CVV)
            .amount(DEFAULT_AMOUNT)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity();
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        cardPayment.setPayment(payment);
        return cardPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardPayment createUpdatedEntity(EntityManager em) {
        CardPayment updatedCardPayment = new CardPayment()
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardExpiryDate(UPDATED_CARD_EXPIRY_DATE)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .cvv(UPDATED_CVV)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createUpdatedEntity();
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        updatedCardPayment.setPayment(payment);
        return updatedCardPayment;
    }

    @BeforeEach
    public void initTest() {
        cardPayment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCardPayment != null) {
            cardPaymentRepository.delete(insertedCardPayment);
            insertedCardPayment = null;
        }
    }

    @Test
    @Transactional
    void createCardPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CardPayment
        var returnedCardPayment = om.readValue(
            restCardPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CardPayment.class
        );

        // Validate the CardPayment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCardPaymentUpdatableFieldsEquals(returnedCardPayment, getPersistedCardPayment(returnedCardPayment));

        insertedCardPayment = returnedCardPayment;
    }

    @Test
    @Transactional
    void createCardPaymentWithExistingId() throws Exception {
        // Create the CardPayment with an existing ID
        cardPayment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCardNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardPayment.setCardNumber(null);

        // Create the CardPayment, which fails.

        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardExpiryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardPayment.setCardExpiryDate(null);

        // Create the CardPayment, which fails.

        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardHolderNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardPayment.setCardHolderName(null);

        // Create the CardPayment, which fails.

        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCvvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardPayment.setCvv(null);

        // Create the CardPayment, which fails.

        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardPayment.setAmount(null);

        // Create the CardPayment, which fails.

        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardPayment.setPaymentDate(null);

        // Create the CardPayment, which fails.

        restCardPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardPayments() throws Exception {
        // Initialize the database
        insertedCardPayment = cardPaymentRepository.saveAndFlush(cardPayment);

        // Get all the cardPaymentList
        restCardPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].cardExpiryDate").value(hasItem(DEFAULT_CARD_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardHolderName").value(hasItem(DEFAULT_CARD_HOLDER_NAME)))
            .andExpect(jsonPath("$.[*].cvv").value(hasItem(DEFAULT_CVV)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCardPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cardPaymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCardPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cardPaymentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCardPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cardPaymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCardPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cardPaymentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCardPayment() throws Exception {
        // Initialize the database
        insertedCardPayment = cardPaymentRepository.saveAndFlush(cardPayment);

        // Get the cardPayment
        restCardPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, cardPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardPayment.getId().intValue()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.cardExpiryDate").value(DEFAULT_CARD_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.cardHolderName").value(DEFAULT_CARD_HOLDER_NAME))
            .andExpect(jsonPath("$.cvv").value(DEFAULT_CVV))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCardPayment() throws Exception {
        // Get the cardPayment
        restCardPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCardPayment() throws Exception {
        // Initialize the database
        insertedCardPayment = cardPaymentRepository.saveAndFlush(cardPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cardPayment
        CardPayment updatedCardPayment = cardPaymentRepository.findById(cardPayment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCardPayment are not directly saved in db
        em.detach(updatedCardPayment);
        updatedCardPayment
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardExpiryDate(UPDATED_CARD_EXPIRY_DATE)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .cvv(UPDATED_CVV)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);

        restCardPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCardPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCardPayment))
            )
            .andExpect(status().isOk());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCardPaymentToMatchAllProperties(updatedCardPayment);
    }

    @Test
    @Transactional
    void putNonExistingCardPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedCardPayment = cardPaymentRepository.saveAndFlush(cardPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cardPayment using partial update
        CardPayment partialUpdatedCardPayment = new CardPayment();
        partialUpdatedCardPayment.setId(cardPayment.getId());

        partialUpdatedCardPayment
            .cardExpiryDate(UPDATED_CARD_EXPIRY_DATE)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);

        restCardPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCardPayment))
            )
            .andExpect(status().isOk());

        // Validate the CardPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardPaymentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCardPayment, cardPayment),
            getPersistedCardPayment(cardPayment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCardPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedCardPayment = cardPaymentRepository.saveAndFlush(cardPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cardPayment using partial update
        CardPayment partialUpdatedCardPayment = new CardPayment();
        partialUpdatedCardPayment.setId(cardPayment.getId());

        partialUpdatedCardPayment
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardExpiryDate(UPDATED_CARD_EXPIRY_DATE)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .cvv(UPDATED_CVV)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);

        restCardPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCardPayment))
            )
            .andExpect(status().isOk());

        // Validate the CardPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardPaymentUpdatableFieldsEquals(partialUpdatedCardPayment, getPersistedCardPayment(partialUpdatedCardPayment));
    }

    @Test
    @Transactional
    void patchNonExistingCardPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cardPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cardPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cardPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCardPayment() throws Exception {
        // Initialize the database
        insertedCardPayment = cardPaymentRepository.saveAndFlush(cardPayment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cardPayment
        restCardPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cardPaymentRepository.count();
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

    protected CardPayment getPersistedCardPayment(CardPayment cardPayment) {
        return cardPaymentRepository.findById(cardPayment.getId()).orElseThrow();
    }

    protected void assertPersistedCardPaymentToMatchAllProperties(CardPayment expectedCardPayment) {
        assertCardPaymentAllPropertiesEquals(expectedCardPayment, getPersistedCardPayment(expectedCardPayment));
    }

    protected void assertPersistedCardPaymentToMatchUpdatableProperties(CardPayment expectedCardPayment) {
        assertCardPaymentAllUpdatablePropertiesEquals(expectedCardPayment, getPersistedCardPayment(expectedCardPayment));
    }
}
