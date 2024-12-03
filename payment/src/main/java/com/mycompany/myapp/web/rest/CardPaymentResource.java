package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CardPayment;
import com.mycompany.myapp.repository.CardPaymentRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CardPayment}.
 */
@RestController
@RequestMapping("/api/card-payments")
@Transactional
public class CardPaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CardPaymentResource.class);

    private static final String ENTITY_NAME = "paymentCardPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardPaymentRepository cardPaymentRepository;

    public CardPaymentResource(CardPaymentRepository cardPaymentRepository) {
        this.cardPaymentRepository = cardPaymentRepository;
    }

    /**
     * {@code POST  /card-payments} : Create a new cardPayment.
     *
     * @param cardPayment the cardPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardPayment, or with status {@code 400 (Bad Request)} if the cardPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CardPayment> createCardPayment(@Valid @RequestBody CardPayment cardPayment) throws URISyntaxException {
        LOG.debug("REST request to save CardPayment : {}", cardPayment);
        if (cardPayment.getId() != null) {
            throw new BadRequestAlertException("A new cardPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cardPayment = cardPaymentRepository.save(cardPayment);
        return ResponseEntity.created(new URI("/api/card-payments/" + cardPayment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cardPayment.getId().toString()))
            .body(cardPayment);
    }

    /**
     * {@code PUT  /card-payments/:id} : Updates an existing cardPayment.
     *
     * @param id the id of the cardPayment to save.
     * @param cardPayment the cardPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardPayment,
     * or with status {@code 400 (Bad Request)} if the cardPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CardPayment> updateCardPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardPayment cardPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to update CardPayment : {}, {}", id, cardPayment);
        if (cardPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cardPayment = cardPaymentRepository.save(cardPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardPayment.getId().toString()))
            .body(cardPayment);
    }

    /**
     * {@code PATCH  /card-payments/:id} : Partial updates given fields of an existing cardPayment, field will ignore if it is null
     *
     * @param id the id of the cardPayment to save.
     * @param cardPayment the cardPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardPayment,
     * or with status {@code 400 (Bad Request)} if the cardPayment is not valid,
     * or with status {@code 404 (Not Found)} if the cardPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardPayment> partialUpdateCardPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardPayment cardPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CardPayment partially : {}, {}", id, cardPayment);
        if (cardPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardPayment> result = cardPaymentRepository
            .findById(cardPayment.getId())
            .map(existingCardPayment -> {
                if (cardPayment.getCardNumber() != null) {
                    existingCardPayment.setCardNumber(cardPayment.getCardNumber());
                }
                if (cardPayment.getCardExpiryDate() != null) {
                    existingCardPayment.setCardExpiryDate(cardPayment.getCardExpiryDate());
                }
                if (cardPayment.getCardHolderName() != null) {
                    existingCardPayment.setCardHolderName(cardPayment.getCardHolderName());
                }
                if (cardPayment.getCvv() != null) {
                    existingCardPayment.setCvv(cardPayment.getCvv());
                }
                if (cardPayment.getAmount() != null) {
                    existingCardPayment.setAmount(cardPayment.getAmount());
                }
                if (cardPayment.getPaymentDate() != null) {
                    existingCardPayment.setPaymentDate(cardPayment.getPaymentDate());
                }
                if (cardPayment.getDescription() != null) {
                    existingCardPayment.setDescription(cardPayment.getDescription());
                }

                return existingCardPayment;
            })
            .map(cardPaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /card-payments} : get all the cardPayments.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardPayments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CardPayment>> getAllCardPayments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CardPayments");
        Page<CardPayment> page;
        if (eagerload) {
            page = cardPaymentRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = cardPaymentRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-payments/:id} : get the "id" cardPayment.
     *
     * @param id the id of the cardPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardPayment> getCardPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CardPayment : {}", id);
        Optional<CardPayment> cardPayment = cardPaymentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cardPayment);
    }

    /**
     * {@code DELETE  /card-payments/:id} : delete the "id" cardPayment.
     *
     * @param id the id of the cardPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CardPayment : {}", id);
        cardPaymentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
