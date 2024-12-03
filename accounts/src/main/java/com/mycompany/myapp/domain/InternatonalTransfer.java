package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CurrencyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A InternatonalTransfer.
 */
@Entity
@Table(name = "internatonal_transfer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InternatonalTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @NotNull
    @Size(min = 15, max = 34)
    @Pattern(regexp = "^[A-Z0-9]+$")
    @Column(name = "recipient_iban", length = 34, nullable = false)
    private String recipientIban;

    @NotNull
    @Size(max = 11)
    @Column(name = "swift_code", length = 11, nullable = false)
    private String swiftCode;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "recipient_name", length = 100, nullable = false)
    private String recipientName;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyType currency;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @Size(max = 225)
    @Column(name = "description", length = 225)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "localtransfers", "internatonalTransfers" }, allowSetters = true)
    private BankAccount bankAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InternatonalTransfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderAccountNumber() {
        return this.senderAccountNumber;
    }

    public InternatonalTransfer senderAccountNumber(String senderAccountNumber) {
        this.setSenderAccountNumber(senderAccountNumber);
        return this;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getRecipientIban() {
        return this.recipientIban;
    }

    public InternatonalTransfer recipientIban(String recipientIban) {
        this.setRecipientIban(recipientIban);
        return this;
    }

    public void setRecipientIban(String recipientIban) {
        this.recipientIban = recipientIban;
    }

    public String getSwiftCode() {
        return this.swiftCode;
    }

    public InternatonalTransfer swiftCode(String swiftCode) {
        this.setSwiftCode(swiftCode);
        return this;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getRecipientName() {
        return this.recipientName;
    }

    public InternatonalTransfer recipientName(String recipientName) {
        this.setRecipientName(recipientName);
        return this;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public InternatonalTransfer amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyType getCurrency() {
        return this.currency;
    }

    public InternatonalTransfer currency(CurrencyType currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public InternatonalTransfer transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return this.description;
    }

    public InternatonalTransfer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public InternatonalTransfer bankAccount(BankAccount bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InternatonalTransfer)) {
            return false;
        }
        return getId() != null && getId().equals(((InternatonalTransfer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InternatonalTransfer{" +
            "id=" + getId() +
            ", senderAccountNumber='" + getSenderAccountNumber() + "'" +
            ", recipientIban='" + getRecipientIban() + "'" +
            ", swiftCode='" + getSwiftCode() + "'" +
            ", recipientName='" + getRecipientName() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
