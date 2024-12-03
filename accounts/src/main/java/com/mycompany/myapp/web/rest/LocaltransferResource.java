package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Localtransfer;
import com.mycompany.myapp.repository.LocaltransferRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Localtransfer}.
 */
@RestController
@RequestMapping("/api/localtransfers")
@Transactional
public class LocaltransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(LocaltransferResource.class);

    private static final String ENTITY_NAME = "accountsLocaltransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocaltransferRepository localtransferRepository;

    public LocaltransferResource(LocaltransferRepository localtransferRepository) {
        this.localtransferRepository = localtransferRepository;
    }

    /**
     * {@code POST  /localtransfers} : Create a new localtransfer.
     *
     * @param localtransfer the localtransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localtransfer, or with status {@code 400 (Bad Request)} if the localtransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Localtransfer> createLocaltransfer(@Valid @RequestBody Localtransfer localtransfer) throws URISyntaxException {
        LOG.debug("REST request to save Localtransfer : {}", localtransfer);
        if (localtransfer.getId() != null) {
            throw new BadRequestAlertException("A new localtransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        localtransfer = localtransferRepository.save(localtransfer);
        return ResponseEntity.created(new URI("/api/localtransfers/" + localtransfer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, localtransfer.getId().toString()))
            .body(localtransfer);
    }

    /**
     * {@code PUT  /localtransfers/:id} : Updates an existing localtransfer.
     *
     * @param id the id of the localtransfer to save.
     * @param localtransfer the localtransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localtransfer,
     * or with status {@code 400 (Bad Request)} if the localtransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localtransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Localtransfer> updateLocaltransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Localtransfer localtransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to update Localtransfer : {}, {}", id, localtransfer);
        if (localtransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localtransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localtransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        localtransfer = localtransferRepository.save(localtransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localtransfer.getId().toString()))
            .body(localtransfer);
    }

    /**
     * {@code PATCH  /localtransfers/:id} : Partial updates given fields of an existing localtransfer, field will ignore if it is null
     *
     * @param id the id of the localtransfer to save.
     * @param localtransfer the localtransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localtransfer,
     * or with status {@code 400 (Bad Request)} if the localtransfer is not valid,
     * or with status {@code 404 (Not Found)} if the localtransfer is not found,
     * or with status {@code 500 (Internal Server Error)} if the localtransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Localtransfer> partialUpdateLocaltransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Localtransfer localtransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Localtransfer partially : {}, {}", id, localtransfer);
        if (localtransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localtransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localtransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Localtransfer> result = localtransferRepository
            .findById(localtransfer.getId())
            .map(existingLocaltransfer -> {
                if (localtransfer.getSenderAccountNumber() != null) {
                    existingLocaltransfer.setSenderAccountNumber(localtransfer.getSenderAccountNumber());
                }
                if (localtransfer.getRecipientAccountNumber() != null) {
                    existingLocaltransfer.setRecipientAccountNumber(localtransfer.getRecipientAccountNumber());
                }
                if (localtransfer.getRecipientBankName() != null) {
                    existingLocaltransfer.setRecipientBankName(localtransfer.getRecipientBankName());
                }
                if (localtransfer.getRecipientBankBranch() != null) {
                    existingLocaltransfer.setRecipientBankBranch(localtransfer.getRecipientBankBranch());
                }
                if (localtransfer.getAmount() != null) {
                    existingLocaltransfer.setAmount(localtransfer.getAmount());
                }
                if (localtransfer.getTransactionDate() != null) {
                    existingLocaltransfer.setTransactionDate(localtransfer.getTransactionDate());
                }
                if (localtransfer.getDescription() != null) {
                    existingLocaltransfer.setDescription(localtransfer.getDescription());
                }

                return existingLocaltransfer;
            })
            .map(localtransferRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localtransfer.getId().toString())
        );
    }

    /**
     * {@code GET  /localtransfers} : get all the localtransfers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localtransfers in body.
     */
    @GetMapping("")
    public ResponseEntity<Localtransfer> getAllLocalTransfers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of LocalTransfers");

        // الحصول على تسجيل دخول المستخدم الحالي
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        Page<Localtransfer> page;

        // التحقق مما إذا كان المستخدم لديه صلاحية ADMIN
        if (SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
            // يمكن للمسؤولين استرداد جميع التحويلات المحلية
            page = localtransferRepository.findAll(pageable);
        } else {
            // يمكن للمستخدمين العاديين استرداد التحويلات المرتبطة فقط بحساباتهم
            page = localtransferRepository.findByBankAccountLogin(currentUserLogin, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body((Localtransfer) page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Localtransfer> getLocalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LocalTransfer : {}", id);

        // الحصول على تسجيل دخول المستخدم الحالي
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        // استرداد التحويل المحلي بناءً على معرفه
        Optional<Localtransfer> localTransfer = localtransferRepository.findById(id);

        // التحقق من وجود التحويل وصلاحية الوصول
        if (localTransfer.isPresent()) {
            Localtransfer foundTransfer = localTransfer.get();

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
     * {@code DELETE  /localtransfers/:id} : delete the "id" localtransfer.
     *
     * @param id the id of the localtransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocaltransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Localtransfer : {}", id);
        localtransferRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
