package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.QRPayment;
import com.mycompany.myapp.repository.QRPaymentRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.QRPayment}.
 */
@RestController
@RequestMapping("/api/qr-payments")
@Transactional
public class QRPaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(QRPaymentResource.class);

    private static final String ENTITY_NAME = "paymentQrPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QRPaymentRepository qRPaymentRepository;

    public QRPaymentResource(QRPaymentRepository qRPaymentRepository) {
        this.qRPaymentRepository = qRPaymentRepository;
    }

    /**
     * {@code POST  /qr-payments} : Create a new qRPayment.
     *
     * @param qRPayment the qRPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qRPayment, or with status {@code 400 (Bad Request)} if the qRPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QRPayment> createQRPayment(@Valid @RequestBody QRPayment qRPayment) throws URISyntaxException {
        LOG.debug("REST request to save QRPayment : {}", qRPayment);
        if (qRPayment.getId() != null) {
            throw new BadRequestAlertException("A new qRPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        qRPayment = qRPaymentRepository.save(qRPayment);
        return ResponseEntity.created(new URI("/api/qr-payments/" + qRPayment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, qRPayment.getId().toString()))
            .body(qRPayment);
    }

    /**
     * {@code PUT  /qr-payments/:id} : Updates an existing qRPayment.
     *
     * @param id the id of the qRPayment to save.
     * @param qRPayment the qRPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qRPayment,
     * or with status {@code 400 (Bad Request)} if the qRPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qRPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QRPayment> updateQRPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QRPayment qRPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to update QRPayment : {}, {}", id, qRPayment);
        if (qRPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qRPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qRPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        qRPayment = qRPaymentRepository.save(qRPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qRPayment.getId().toString()))
            .body(qRPayment);
    }

    /**
     * {@code PATCH  /qr-payments/:id} : Partial updates given fields of an existing qRPayment, field will ignore if it is null
     *
     * @param id the id of the qRPayment to save.
     * @param qRPayment the qRPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qRPayment,
     * or with status {@code 400 (Bad Request)} if the qRPayment is not valid,
     * or with status {@code 404 (Not Found)} if the qRPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the qRPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QRPayment> partialUpdateQRPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QRPayment qRPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QRPayment partially : {}, {}", id, qRPayment);
        if (qRPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qRPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qRPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QRPayment> result = qRPaymentRepository
            .findById(qRPayment.getId())
            .map(existingQRPayment -> {
                if (qRPayment.getQrCode() != null) {
                    existingQRPayment.setQrCode(qRPayment.getQrCode());
                }
                if (qRPayment.getAmount() != null) {
                    existingQRPayment.setAmount(qRPayment.getAmount());
                }
                if (qRPayment.getPaymentDate() != null) {
                    existingQRPayment.setPaymentDate(qRPayment.getPaymentDate());
                }
                if (qRPayment.getDescription() != null) {
                    existingQRPayment.setDescription(qRPayment.getDescription());
                }

                return existingQRPayment;
            })
            .map(qRPaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qRPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /qr-payments} : get all the qRPayments.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qRPayments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QRPayment>> getAllQRPayments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of QRPayments");
        Page<QRPayment> page;
        if (eagerload) {
            page = qRPaymentRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = qRPaymentRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qr-payments/:id} : get the "id" qRPayment.
     *
     * @param id the id of the qRPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qRPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QRPayment> getQRPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QRPayment : {}", id);
        Optional<QRPayment> qRPayment = qRPaymentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(qRPayment);
    }

    /**
     * {@code DELETE  /qr-payments/:id} : delete the "id" qRPayment.
     *
     * @param id the id of the qRPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQRPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QRPayment : {}", id);
        qRPaymentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
