package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.PaymentStatus;
import com.mycompany.myapp.domain.enumeration.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "transaction_id", length = 36, nullable = false, unique = true)
    private UUID transactionId;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Size(min = 3, max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @NotNull
    @Pattern(regexp = "^[0-9A-Z-]+$")
    @Column(name = "payment_id", nullable = false, unique = true)
    private String paymentId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<QRPayment> qRPayments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<CardPayment> cardPayments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<BillPayment> billPayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTransactionId() {
        return this.transactionId;
    }

    public Payment transactionId(UUID transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getLogin() {
        return this.login;
    }

    public Payment login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public Payment paymentType(PaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Payment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public Payment status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Payment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Payment createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Payment lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Payment lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    public Payment paymentId(String paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Set<QRPayment> getQRPayments() {
        return this.qRPayments;
    }

    public void setQRPayments(Set<QRPayment> qRPayments) {
        if (this.qRPayments != null) {
            this.qRPayments.forEach(i -> i.setPayment(null));
        }
        if (qRPayments != null) {
            qRPayments.forEach(i -> i.setPayment(this));
        }
        this.qRPayments = qRPayments;
    }

    public Payment qRPayments(Set<QRPayment> qRPayments) {
        this.setQRPayments(qRPayments);
        return this;
    }

    public Payment addQRPayment(QRPayment qRPayment) {
        this.qRPayments.add(qRPayment);
        qRPayment.setPayment(this);
        return this;
    }

    public Payment removeQRPayment(QRPayment qRPayment) {
        this.qRPayments.remove(qRPayment);
        qRPayment.setPayment(null);
        return this;
    }

    public Set<CardPayment> getCardPayments() {
        return this.cardPayments;
    }

    public void setCardPayments(Set<CardPayment> cardPayments) {
        if (this.cardPayments != null) {
            this.cardPayments.forEach(i -> i.setPayment(null));
        }
        if (cardPayments != null) {
            cardPayments.forEach(i -> i.setPayment(this));
        }
        this.cardPayments = cardPayments;
    }

    public Payment cardPayments(Set<CardPayment> cardPayments) {
        this.setCardPayments(cardPayments);
        return this;
    }

    public Payment addCardPayment(CardPayment cardPayment) {
        this.cardPayments.add(cardPayment);
        cardPayment.setPayment(this);
        return this;
    }

    public Payment removeCardPayment(CardPayment cardPayment) {
        this.cardPayments.remove(cardPayment);
        cardPayment.setPayment(null);
        return this;
    }

    public Set<BillPayment> getBillPayments() {
        return this.billPayments;
    }

    public void setBillPayments(Set<BillPayment> billPayments) {
        if (this.billPayments != null) {
            this.billPayments.forEach(i -> i.setPayment(null));
        }
        if (billPayments != null) {
            billPayments.forEach(i -> i.setPayment(this));
        }
        this.billPayments = billPayments;
    }

    public Payment billPayments(Set<BillPayment> billPayments) {
        this.setBillPayments(billPayments);
        return this;
    }

    public Payment addBillPayment(BillPayment billPayment) {
        this.billPayments.add(billPayment);
        billPayment.setPayment(this);
        return this;
    }

    public Payment removeBillPayment(BillPayment billPayment) {
        this.billPayments.remove(billPayment);
        billPayment.setPayment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", transactionId='" + getTransactionId() + "'" +
            ", login='" + getLogin() + "'" +
            ", amount=" + getAmount() +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", paymentId='" + getPaymentId() + "'" +
            "}";
    }
}
