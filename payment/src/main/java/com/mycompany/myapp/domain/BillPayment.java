package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A BillPayment.
 */
@Entity
@Table(name = "bill_payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "bill_reference", length = 50, nullable = false)
    private String billReference;

    @NotNull
    @Size(max = 100)
    @Column(name = "bill_issuer", length = 100, nullable = false)
    private String billIssuer;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "qRPayments", "cardPayments", "billPayments" }, allowSetters = true)
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillReference() {
        return this.billReference;
    }

    public BillPayment billReference(String billReference) {
        this.setBillReference(billReference);
        return this;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getBillIssuer() {
        return this.billIssuer;
    }

    public BillPayment billIssuer(String billIssuer) {
        this.setBillIssuer(billIssuer);
        return this;
    }

    public void setBillIssuer(String billIssuer) {
        this.billIssuer = billIssuer;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BillPayment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public BillPayment paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return this.description;
    }

    public BillPayment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BillPayment payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillPayment)) {
            return false;
        }
        return getId() != null && getId().equals(((BillPayment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillPayment{" +
            "id=" + getId() +
            ", billReference='" + getBillReference() + "'" +
            ", billIssuer='" + getBillIssuer() + "'" +
            ", amount=" + getAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
