package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.InternatonalTransfer;
import com.mycompany.myapp.repository.InternatonalTransferRepository;
import com.mycompany.myapp.security.SecurityUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.InternatonalTransfer}.
 */
@RestController
@RequestMapping("/api/internatonal-transfers")
@Transactional
public class InternatonalTransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(InternatonalTransferResource.class);

    private static final String ENTITY_NAME = "accountsInternatonalTransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternatonalTransferRepository internatonalTransferRepository;

    public InternatonalTransferResource(InternatonalTransferRepository internatonalTransferRepository) {
        this.internatonalTransferRepository = internatonalTransferRepository;
    }

    /**
     * {@code POST  /internatonal-transfers} : Create a new internatonalTransfer.
     *
     * @param internatonalTransfer the internatonalTransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internatonalTransfer, or with status {@code 400 (Bad Request)} if the internatonalTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InternatonalTransfer> createInternatonalTransfer(@Valid @RequestBody InternatonalTransfer internatonalTransfer)
        throws URISyntaxException {
        LOG.debug("REST request to save InternatonalTransfer : {}", internatonalTransfer);
        if (internatonalTransfer.getId() != null) {
            throw new BadRequestAlertException("A new internatonalTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        internatonalTransfer = internatonalTransferRepository.save(internatonalTransfer);
        return ResponseEntity.created(new URI("/api/internatonal-transfers/" + internatonalTransfer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, internatonalTransfer.getId().toString()))
            .body(internatonalTransfer);
    }

    /**
     * {@code PUT  /internatonal-transfers/:id} : Updates an existing internatonalTransfer.
     *
     * @param id the id of the internatonalTransfer to save.
     * @param internatonalTransfer the internatonalTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internatonalTransfer,
     * or with status {@code 400 (Bad Request)} if the internatonalTransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internatonalTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InternatonalTransfer> updateInternatonalTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InternatonalTransfer internatonalTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to update InternatonalTransfer : {}, {}", id, internatonalTransfer);
        if (internatonalTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internatonalTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internatonalTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        internatonalTransfer = internatonalTransferRepository.save(internatonalTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, internatonalTransfer.getId().toString()))
            .body(internatonalTransfer);
    }

    /**
     * {@code PATCH  /internatonal-transfers/:id} : Partial updates given fields of an existing internatonalTransfer, field will ignore if it is null
     *
     * @param id the id of the internatonalTransfer to save.
     * @param internatonalTransfer the internatonalTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internatonalTransfer,
     * or with status {@code 400 (Bad Request)} if the internatonalTransfer is not valid,
     * or with status {@code 404 (Not Found)} if the internatonalTransfer is not found,
     * or with status {@code 500 (Internal Server Error)} if the internatonalTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InternatonalTransfer> partialUpdateInternatonalTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InternatonalTransfer internatonalTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InternatonalTransfer partially : {}, {}", id, internatonalTransfer);
        if (internatonalTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internatonalTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internatonalTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InternatonalTransfer> result = internatonalTransferRepository
            .findById(internatonalTransfer.getId())
            .map(existingInternatonalTransfer -> {
                if (internatonalTransfer.getSenderAccountNumber() != null) {
                    existingInternatonalTransfer.setSenderAccountNumber(internatonalTransfer.getSenderAccountNumber());
                }
                if (internatonalTransfer.getRecipientIban() != null) {
                    existingInternatonalTransfer.setRecipientIban(internatonalTransfer.getRecipientIban());
                }
                if (internatonalTransfer.getSwiftCode() != null) {
                    existingInternatonalTransfer.setSwiftCode(internatonalTransfer.getSwiftCode());
                }
                if (internatonalTransfer.getRecipientName() != null) {
                    existingInternatonalTransfer.setRecipientName(internatonalTransfer.getRecipientName());
                }
                if (internatonalTransfer.getAmount() != null) {
                    existingInternatonalTransfer.setAmount(internatonalTransfer.getAmount());
                }
                if (internatonalTransfer.getCurrency() != null) {
                    existingInternatonalTransfer.setCurrency(internatonalTransfer.getCurrency());
                }
                if (internatonalTransfer.getTransactionDate() != null) {
                    existingInternatonalTransfer.setTransactionDate(internatonalTransfer.getTransactionDate());
                }
                if (internatonalTransfer.getDescription() != null) {
                    existingInternatonalTransfer.setDescription(internatonalTransfer.getDescription());
                }

                return existingInternatonalTransfer;
            })
            .map(internatonalTransferRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, internatonalTransfer.getId().toString())
        );
    }

    /**
     * {@code GET  /internatonal-transfers} : get all the internatonalTransfers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internatonalTransfers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InternatonalTransfer>> getAllInternationalTransfers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of InternationalTransfers");

        // الحصول على تسجيل دخول المستخدم الحالي
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        Page<InternatonalTransfer> page;

        // التحقق مما إذا كان المستخدم لديه صلاحية ADMIN
        if (SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
            // يمكن للمسؤولين استرداد جميع التحويلات الدولية
            page = internatonalTransferRepository.findAll(pageable);
        } else {
            // يمكن للمستخدمين العاديين استرداد التحويلات المرتبطة فقط بحساباتهم
            page = internatonalTransferRepository.findByBankAccountLogin(currentUserLogin, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternatonalTransfer> getInternationalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InternationalTransfer : {}", id);

        // الحصول على تسجيل دخول المستخدم الحالي
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        // استرداد التحويل الدولي بناءً على معرفه
        Optional<InternatonalTransfer> internationalTransfer = internatonalTransferRepository.findById(id);

        // التحقق من وجود التحويل وصلاحية الوصول
        if (internationalTransfer.isPresent()) {
            InternatonalTransfer foundTransfer = internationalTransfer.get();

            // السماح بالوصول إذا كان المستخدم مسؤولًا أو إذا كان التحويل مرتبطًا بحساب مرتبط بتسجيل الدخول الحالي
            if (SecurityUtils.isCurrentUserInRole("ROLE_ADMIN") || foundTransfer.getBankAccount().getLogin().equals(currentUserLogin)) {
                return ResponseEntity.ok(foundTransfer);
            } else {
                // رفض الوصول إذا لم يكن المستخدم مصرحًا له
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * {@code DELETE  /internatonal-transfers/:id} : delete the "id" internatonalTransfer.
     *
     * @param id the id of the internatonalTransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternatonalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InternatonalTransfer : {}", id);
        internatonalTransferRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
