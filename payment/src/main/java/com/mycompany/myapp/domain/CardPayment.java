package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A CardPayment.
 */
@Entity
@Table(name = "card_payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^[0-9]{16}$")
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @NotNull
    @Column(name = "card_expiry_date", nullable = false)
    private LocalDate cardExpiryDate;

    @NotNull
    @Size(max = 100)
    @Column(name = "card_holder_name", length = 100, nullable = false)
    private String cardHolderName;

    @NotNull
    @Pattern(regexp = "^[0-9]{3,4}$")
    @Column(name = "cvv", nullable = false)
    private String cvv;

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

    public CardPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public CardPayment cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getCardExpiryDate() {
        return this.cardExpiryDate;
    }

    public CardPayment cardExpiryDate(LocalDate cardExpiryDate) {
        this.setCardExpiryDate(cardExpiryDate);
        return this;
    }

    public void setCardExpiryDate(LocalDate cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public CardPayment cardHolderName(String cardHolderName) {
        this.setCardHolderName(cardHolderName);
        return this;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCvv() {
        return this.cvv;
    }

    public CardPayment cvv(String cvv) {
        this.setCvv(cvv);
        return this;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public CardPayment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public CardPayment paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return this.description;
    }

    public CardPayment description(String description) {
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

    public CardPayment payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardPayment)) {
            return false;
        }
        return getId() != null && getId().equals(((CardPayment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardPayment{" +
            "id=" + getId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", cardExpiryDate='" + getCardExpiryDate() + "'" +
            ", cardHolderName='" + getCardHolderName() + "'" +
            ", cvv='" + getCvv() + "'" +
            ", amount=" + getAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
