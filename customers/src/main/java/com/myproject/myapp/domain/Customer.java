package com.myproject.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myproject.myapp.domain.enumeration.CityType;
import com.myproject.myapp.domain.enumeration.EmploymentStatus;
import com.myproject.myapp.domain.enumeration.Gender;
import com.myproject.myapp.domain.enumeration.NationalityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "mother_name", length = 100, nullable = false)
    private String motherName;

    @NotNull
    @Size(min = 12, max = 12)
    @Pattern(regexp = "^[0-9]{12}$")
    @Column(name = "national_id", length = 12, nullable = false)
    private String nationalId;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Size(min = 10, max = 15)
    @Pattern(regexp = "^[+]?[0-9]{10,15}$")
    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    @NotNull
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Size(max = 200)
    @Column(name = "address_line_1", length = 200, nullable = false)
    private String addressLine1;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false)
    private EmploymentStatus employmentStatus;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private Instant registrationDate;

    @NotNull
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false)
    private NationalityType nationality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "city", nullable = false)
    private CityType city;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Customer login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Customer fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMotherName() {
        return this.motherName;
    }

    public Customer motherName(String motherName) {
        this.setMotherName(motherName);
        return this;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public Customer nationalId(String nationalId) {
        this.setNationalId(nationalId);
        return this;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Customer dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Customer gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Customer addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public EmploymentStatus getEmploymentStatus() {
        return this.employmentStatus;
    }

    public Customer employmentStatus(EmploymentStatus employmentStatus) {
        this.setEmploymentStatus(employmentStatus);
        return this;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Instant getRegistrationDate() {
        return this.registrationDate;
    }

    public Customer registrationDate(Instant registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Customer lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public NationalityType getNationality() {
        return this.nationality;
    }

    public Customer nationality(NationalityType nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(NationalityType nationality) {
        this.nationality = nationality;
    }

    public CityType getCity() {
        return this.city;
    }

    public Customer city(CityType city) {
        this.setCity(city);
        return this;
    }

    public void setCity(CityType city) {
        this.city = city;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setCustomer(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setCustomer(this));
        }
        this.documents = documents;
    }

    public Customer documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Customer addDocument(Document document) {
        this.documents.add(document);
        document.setCustomer(this);
        return this;
    }

    public Customer removeDocument(Document document) {
        this.documents.remove(document);
        document.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", nationalId='" + getNationalId() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", employmentStatus='" + getEmploymentStatus() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
