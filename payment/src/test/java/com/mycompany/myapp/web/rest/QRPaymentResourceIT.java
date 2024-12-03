package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QRPaymentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Payment;
import com.mycompany.myapp.domain.QRPayment;
import com.mycompany.myapp.repository.QRPaymentRepository;
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
 * Integration tests for the {@link QRPaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QRPaymentResourceIT {

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/qr-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QRPaymentRepository qRPaymentRepository;

    @Mock
    private QRPaymentRepository qRPaymentRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQRPaymentMockMvc;

    private QRPayment qRPayment;

    private QRPayment insertedQRPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QRPayment createEntity(EntityManager em) {
        QRPayment qRPayment = new QRPayment()
            .qrCode(DEFAULT_QR_CODE)
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
        qRPayment.setPayment(payment);
        return qRPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QRPayment createUpdatedEntity(EntityManager em) {
        QRPayment updatedQRPayment = new QRPayment()
            .qrCode(UPDATED_QR_CODE)
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
        updatedQRPayment.setPayment(payment);
        return updatedQRPayment;
    }

    @BeforeEach
    public void initTest() {
        qRPayment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedQRPayment != null) {
            qRPaymentRepository.delete(insertedQRPayment);
            insertedQRPayment = null;
        }
    }

    @Test
    @Transactional
    void createQRPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QRPayment
        var returnedQRPayment = om.readValue(
            restQRPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QRPayment.class
        );

        // Validate the QRPayment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQRPaymentUpdatableFieldsEquals(returnedQRPayment, getPersistedQRPayment(returnedQRPayment));

        insertedQRPayment = returnedQRPayment;
    }

    @Test
    @Transactional
    void createQRPaymentWithExistingId() throws Exception {
        // Create the QRPayment with an existing ID
        qRPayment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQRPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment)))
            .andExpect(status().isBadRequest());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQrCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        qRPayment.setQrCode(null);

        // Create the QRPayment, which fails.

        restQRPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        qRPayment.setAmount(null);

        // Create the QRPayment, which fails.

        restQRPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        qRPayment.setPaymentDate(null);

        // Create the QRPayment, which fails.

        restQRPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQRPayments() throws Exception {
        // Initialize the database
        insertedQRPayment = qRPaymentRepository.saveAndFlush(qRPayment);

        // Get all the qRPaymentList
        restQRPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qRPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQRPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(qRPaymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQRPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(qRPaymentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQRPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(qRPaymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQRPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(qRPaymentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQRPayment() throws Exception {
        // Initialize the database
        insertedQRPayment = qRPaymentRepository.saveAndFlush(qRPayment);

        // Get the qRPayment
        restQRPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, qRPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qRPayment.getId().intValue()))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingQRPayment() throws Exception {
        // Get the qRPayment
        restQRPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQRPayment() throws Exception {
        // Initialize the database
        insertedQRPayment = qRPaymentRepository.saveAndFlush(qRPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRPayment
        QRPayment updatedQRPayment = qRPaymentRepository.findById(qRPayment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQRPayment are not directly saved in db
        em.detach(updatedQRPayment);
        updatedQRPayment.qrCode(UPDATED_QR_CODE).amount(UPDATED_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE).description(UPDATED_DESCRIPTION);

        restQRPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQRPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQRPayment))
            )
            .andExpect(status().isOk());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQRPaymentToMatchAllProperties(updatedQRPayment);
    }

    @Test
    @Transactional
    void putNonExistingQRPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQRPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qRPayment.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQRPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(qRPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQRPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQRPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedQRPayment = qRPaymentRepository.saveAndFlush(qRPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRPayment using partial update
        QRPayment partialUpdatedQRPayment = new QRPayment();
        partialUpdatedQRPayment.setId(qRPayment.getId());

        partialUpdatedQRPayment.qrCode(UPDATED_QR_CODE).paymentDate(UPDATED_PAYMENT_DATE);

        restQRPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQRPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQRPayment))
            )
            .andExpect(status().isOk());

        // Validate the QRPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQRPaymentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQRPayment, qRPayment),
            getPersistedQRPayment(qRPayment)
        );
    }

    @Test
    @Transactional
    void fullUpdateQRPaymentWithPatch() throws Exception {
        // Initialize the database
        insertedQRPayment = qRPaymentRepository.saveAndFlush(qRPayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRPayment using partial update
        QRPayment partialUpdatedQRPayment = new QRPayment();
        partialUpdatedQRPayment.setId(qRPayment.getId());

        partialUpdatedQRPayment
            .qrCode(UPDATED_QR_CODE)
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION);

        restQRPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQRPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQRPayment))
            )
            .andExpect(status().isOk());

        // Validate the QRPayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQRPaymentUpdatableFieldsEquals(partialUpdatedQRPayment, getPersistedQRPayment(partialUpdatedQRPayment));
    }

    @Test
    @Transactional
    void patchNonExistingQRPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRPayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQRPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, qRPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(qRPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQRPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(qRPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQRPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRPayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(qRPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QRPayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQRPayment() throws Exception {
        // Initialize the database
        insertedQRPayment = qRPaymentRepository.saveAndFlush(qRPayment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the qRPayment
        restQRPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, qRPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return qRPaymentRepository.count();
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

    protected QRPayment getPersistedQRPayment(QRPayment qRPayment) {
        return qRPaymentRepository.findById(qRPayment.getId()).orElseThrow();
    }

    protected void assertPersistedQRPaymentToMatchAllProperties(QRPayment expectedQRPayment) {
        assertQRPaymentAllPropertiesEquals(expectedQRPayment, getPersistedQRPayment(expectedQRPayment));
    }

    protected void assertPersistedQRPaymentToMatchUpdatableProperties(QRPayment expectedQRPayment) {
        assertQRPaymentAllUpdatablePropertiesEquals(expectedQRPayment, getPersistedQRPayment(expectedQRPayment));
    }
}
