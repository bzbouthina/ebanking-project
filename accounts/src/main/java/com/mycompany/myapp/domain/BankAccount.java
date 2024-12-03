package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.AccountStatus;
import com.mycompany.myapp.domain.enumeration.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A BankAccount.
 */
@Entity
@Table(name = "bank_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @NotNull
    @Size(min = 10, max = 20)
    @Pattern(regexp = "^[0-9A-Z]+$")
    @Column(name = "account_number", length = 20, nullable = false, unique = true)
    private String accountNumber;

    @Size(min = 15, max = 34)
    @Pattern(regexp = "^[A-Z0-9]+$")
    @Column(name = "iban", length = 34, unique = true)
    private String iban;

    @NotNull
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @NotNull
    @Size(max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @NotNull
    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "branch", length = 50, nullable = false)
    private String branch;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bankAccount")
    @JsonIgnoreProperties(value = { "bankAccount" }, allowSetters = true)
    private Set<Localtransfer> localtransfers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bankAccount")
    @JsonIgnoreProperties(value = { "bankAccount" }, allowSetters = true)
    private Set<InternatonalTransfer> internatonalTransfers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public BankAccount login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public BankAccount accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIban() {
        return this.iban;
    }

    public BankAccount iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BankAccount balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public BankAccount currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    public BankAccount openingDate(LocalDate openingDate) {
        this.setOpeningDate(openingDate);
        return this;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public AccountStatus getStatus() {
        return this.status;
    }

    public BankAccount status(AccountStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public BankAccount accountType(AccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getBranch() {
        return this.branch;
    }

    public BankAccount branch(String branch) {
        this.setBranch(branch);
        return this;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Set<Localtransfer> getLocaltransfers() {
        return this.localtransfers;
    }

    public void setLocaltransfers(Set<Localtransfer> localtransfers) {
        if (this.localtransfers != null) {
            this.localtransfers.forEach(i -> i.setBankAccount(null));
        }
        if (localtransfers != null) {
            localtransfers.forEach(i -> i.setBankAccount(this));
        }
        this.localtransfers = localtransfers;
    }

    public BankAccount localtransfers(Set<Localtransfer> localtransfers) {
        this.setLocaltransfers(localtransfers);
        return this;
    }

    public BankAccount addLocaltransfer(Localtransfer localtransfer) {
        this.localtransfers.add(localtransfer);
        localtransfer.setBankAccount(this);
        return this;
    }

    public BankAccount removeLocaltransfer(Localtransfer localtransfer) {
        this.localtransfers.remove(localtransfer);
        localtransfer.setBankAccount(null);
        return this;
    }

    public Set<InternatonalTransfer> getInternatonalTransfers() {
        return this.internatonalTransfers;
    }

    public void setInternatonalTransfers(Set<InternatonalTransfer> internatonalTransfers) {
        if (this.internatonalTransfers != null) {
            this.internatonalTransfers.forEach(i -> i.setBankAccount(null));
        }
        if (internatonalTransfers != null) {
            internatonalTransfers.forEach(i -> i.setBankAccount(this));
        }
        this.internatonalTransfers = internatonalTransfers;
    }

    public BankAccount internatonalTransfers(Set<InternatonalTransfer> internatonalTransfers) {
        this.setInternatonalTransfers(internatonalTransfers);
        return this;
    }

    public BankAccount addInternatonalTransfer(InternatonalTransfer internatonalTransfer) {
        this.internatonalTransfers.add(internatonalTransfer);
        internatonalTransfer.setBankAccount(this);
        return this;
    }

    public BankAccount removeInternatonalTransfer(InternatonalTransfer internatonalTransfer) {
        this.internatonalTransfers.remove(internatonalTransfer);
        internatonalTransfer.setBankAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((BankAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccount{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", iban='" + getIban() + "'" +
            ", balance=" + getBalance() +
            ", currency='" + getCurrency() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", branch='" + getBranch() + "'" +
            "}";
    }
}
