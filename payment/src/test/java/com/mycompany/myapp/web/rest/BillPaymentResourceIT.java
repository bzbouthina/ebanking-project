package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.BillPaymentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BillPayment;
import com.mycompany.myapp.domain.Payment;
import com.mycompany.myapp.repository.BillPaymentRepository;
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
 * Integration tests for the {@link BillPaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BillPaymentResourceIT {

    private static final String DEFAULT_BILL_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_BILL_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_ISSUER = "AAAAAAAAAA";
    private static final String UPDATED_BILL_ISSUER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bill-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Mock
    private BillPaymentRepository billPaymentRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillPaymentMockMvc;

    private BillPayment billPayment;

    private BillPayment insertedBillPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillPayment createEntity(EntityManager em) {
        BillPayment billPayment = new BillPayment()
            .billReference(DEFAULT_BILL_REFERENCE)
            .billIssuer(DEFAULT_BILL_ISSUER)
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
        billPayment.setPayment(payment);
        return billPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillPayment createUpdatedEntity(EntityManager em) {
        BillPayment updatedBillPayment = new BillPayment()
            .billReference(UPDATED_BILL_REFERENCE)
            .billIssuer(UPDATED_BILL_ISSUER)
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
        updatedBillPayment.setPayment(payment);
        return updatedBillPayment;
    }

    @BeforeEach
    public void initTest() {
        billPayment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBillPayment != null) {
            billPaymentRepository.delete(insertedBillPayment);
            insertedBillPayment = null;
        }
    }

    @Test
    @Transactional
    void createBillPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BillPayment
        var returnedBillPayment = om.readValue(
            restBillPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillPayment.class
        );

        // Validate the BillPayment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBillPaymentUpdatableFieldsEquals(returnedBillPayment, getPersistedBillPayment(returnedBillPayment));

        insertedBillPayment = returnedBillPayment;
    }

    @Test
    @Transactional
    void createBillPaymentWithExistingId() throws Exception {
        // Create the BillPayment with an existing ID
        billPayment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBillReferenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billPayment.setBillReference(null);

        // Create the BillPayment, which fails.

        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBillIssuerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billPayment.setBillIssuer(null);

        // Create the BillPayment, which fails.

        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billPayment.setAmount(null);

        // Create the BillPayment, which fails.

        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billPayment.setPaymentDate(null);

        // Create the BillPayment, which fails.

        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBillPayments() throws Exception {
        // Initialize the database
        insertedBillPayment = billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList
        restBillPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].billReference").value(hasItem(DEFAULT_BILL_REFERENCE)))
            .andExpect(jsonPath("$.[*].billIssuer").value(hasItem(DEFAULT_BILL_ISSUER)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(billPaymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(billPaymentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(billPaymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(billPaymentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBillPayment() throws Exception {
        // Initialize the database
        insertedBillPayment = billPaymentRepository.saveAndFlush(billPayment);

        // Get the billPayment
        restBillPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, billPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billPayment.getId().intValue()))
            .andExpect(jsonPath("$.billReference").value(DEFAULT_BILL_REFERENCE))
            .andExpect(jsonPath("$.billIssuer").value(DEFAULT_BILL_ISSUER))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBillPayment() throws Exception {
        // Get the billPayment
        restBillPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillPayment() throws Exception {
        // Initialize the database
        insertedBillPayment = billPaymentRepository.saveAndFlush(billPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billPayment
        BillPayment updatedBillPayment = billPaymentRepository.findById(billPayment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBillPayment are not directly saved in db
        em.detach(updatedBillPayment);
        updatedBillPayment
            .billReference(UPDATED_BILL_REFERENCE)
            .billIssuer(UPDATED_BILL_ISSUER)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);

        restBillPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBillPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBillPayment))
            )
            .andExpect(status().isOk());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillPaymentToMatchAllProperties(updatedBillPayment);
    }

    @Test
    @Transactional
    void putNonExistingBillPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedBillPayment = billPaymentRepository.saveAndFlush(billPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billPayment using partial update
        BillPayment partialUpdatedBillPayment = new BillPayment();
        partialUpdatedBillPayment.setId(billPayment.getId());

        partialUpdatedBillPayment.description(UPDATED_DESCRIPTION);

        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillPayment))
            )
            .andExpect(status().isOk());

        // Validate the BillPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillPaymentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBillPayment, billPayment),
            getPersistedBillPayment(billPayment)
        );
    }

    @Test
    @Transactional
    void fullUpdateBillPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedBillPayment = billPaymentRepository.saveAndFlush(billPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billPayment using partial update
        BillPayment partialUpdatedBillPayment = new BillPayment();
        partialUpdatedBillPayment.setId(billPayment.getId());

        partialUpdatedBillPayment
            .billReference(UPDATED_BILL_REFERENCE)
            .billIssuer(UPDATED_BILL_ISSUER)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);

        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillPayment))
            )
            .andExpect(status().isOk());

        // Validate the BillPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillPaymentUpdatableFieldsEquals(partialUpdatedBillPayment, getPersistedBillPayment(partialUpdatedBillPayment));
    }

    @Test
    @Transactional
    void patchNonExistingBillPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillPayment() throws Exception {
        // Initialize the database
        insertedBillPayment = billPaymentRepository.saveAndFlush(billPayment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billPayment
        restBillPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, billPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billPaymentRepository.count();
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

    protected BillPayment getPersistedBillPayment(BillPayment billPayment) {
        return billPaymentRepository.findById(billPayment.getId()).orElseThrow();
    }

    protected void assertPersistedBillPaymentToMatchAllProperties(BillPayment expectedBillPayment) {
        assertBillPaymentAllPropertiesEquals(expectedBillPayment, getPersistedBillPayment(expectedBillPayment));
    }

    protected void assertPersistedBillPaymentToMatchUpdatableProperties(BillPayment expectedBillPayment) {
        assertBillPaymentAllUpdatablePropertiesEquals(expectedBillPayment, getPersistedBillPayment(expectedBillPayment));
    }
}
