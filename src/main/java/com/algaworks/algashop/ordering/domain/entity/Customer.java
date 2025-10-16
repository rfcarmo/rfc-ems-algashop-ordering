package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.validator.FieldValidations;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.*;

public class Customer implements Serializable {

    private CustomerId id;
    private FullName fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private LoyaltyPoints loyaltyPoints;
    private Address address;

    public Customer(CustomerId id, FullName fullName, LocalDate birthDate, String email, String phone, String document,
                    Boolean promotionNotificationsAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt,
                    LoyaltyPoints loyaltyPoints, Address address) {
        this.setId(id);
        this.setFullName(fullName);
        this.setBirthDate(birthDate);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
        this.setArchived(archived);
        this.setRegisteredAt(registeredAt);
        this.setArchivedAt(archivedAt);
        this.setLoyaltyPoints(loyaltyPoints);
        this.setAddress(address);
    }

    public Customer(CustomerId id, FullName fullName, LocalDate birthDate, String email, String phone, String document,
                    Boolean promotionNotificationsAllowed, OffsetDateTime registeredAt, Address address) {
        this.setId(id);
        this.setFullName(fullName);
        this.setBirthDate(birthDate);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
        this.setRegisteredAt(registeredAt);
        this.setArchived(false);
        this.setLoyaltyPoints(LoyaltyPoints.ZERO);
        this.setAddress(address);
    }

    public void addLoyaltyPoints(int points) {
        verifyIfIsChangeble();

        this.setLoyaltyPoints(this.loyaltyPoints().add(new LoyaltyPoints(points)));
    }

    public void archive() {
        verifyIfIsChangeble();

        this.setArchived(true);
        this.setArchivedAt(OffsetDateTime.now());
        this.setFullName(new FullName("Anonymous", "Customer"));
        this.setEmail("archived_" + UUID.randomUUID() + "@anonymus.com");
        this.setPhone("000000000");
        this.setDocument("XXXXXXXX");
        this.setBirthDate(null);
        this.setPromotionNotificationsAllowed(false);

        Address.AddressBuilder addressBuilder = this.address().toBuilder();
        this.setAddress(addressBuilder.number("Anonymized").complement(null).build());
    }

    public void enablePromotionNotifications() {
        verifyIfIsChangeble();

        this.setPromotionNotificationsAllowed(true);
    }

    public void disablePromotionNotifications() {
        verifyIfIsChangeble();

        this.setPromotionNotificationsAllowed(false);
    }

    public void changeName(FullName fullName) {
        verifyIfIsChangeble();

        this.setFullName(fullName);
    }

    public void changeEmail(String email) {
        verifyIfIsChangeble();

        this.setEmail(email);
    }

    public void changePhone(String phone) {
        verifyIfIsChangeble();

        this.setPhone(phone);
    }

    public void changeAddress(Address address) {
        verifyIfIsChangeble();

        this.setAddress(address);
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public LocalDate birthDate() {
        return birthDate;
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public String document() {
        return document;
    }

    public Boolean isPromotionNotificationsAllowed() {
        return promotionNotificationsAllowed;
    }

    public Boolean isArchived() {
        return archived;
    }

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    public OffsetDateTime archivedAt() {
        return archivedAt;
    }

    public LoyaltyPoints loyaltyPoints() {
        return loyaltyPoints;
    }

    public Address address() {
        return address;
    }

    private void setId(CustomerId id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    private void setFullName(FullName fullName) {
        Objects.requireNonNull(fullName, VALIDATION_ERROR_FULLNAME_IS_NULL);

        this.fullName = fullName;
    }

    private void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            this.birthDate = null;
            return;
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
        }

        this.birthDate = birthDate;
    }

    private void setEmail(String email) {
        FieldValidations.requiresValidEmail(email, VALIDATION_ERROR_INVALID_EMAIL);

        this.email = email;
    }

    private void setPhone(String phone) {
        Objects.requireNonNull(phone);

        this.phone = phone;
    }

    private void setDocument(String document) {
        Objects.requireNonNull(document);

        this.document = document;
    }

    private void setPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
        Objects.requireNonNull(promotionNotificationsAllowed);

        this.promotionNotificationsAllowed = promotionNotificationsAllowed;
    }

    private void setArchived(Boolean archived) {
        Objects.requireNonNull(archived);

        this.archived = archived;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        Objects.requireNonNull(registeredAt);

        this.registeredAt = registeredAt;
    }

    private void setArchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
        Objects.requireNonNull(loyaltyPoints);

        this.loyaltyPoints = loyaltyPoints;
    }

    private void setAddress(Address address) {
        Objects.requireNonNull(address);

        this.address = address;
    }

    private void verifyIfIsChangeble() {
        if (this.isArchived()) {
            throw new CustomerArchivedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
