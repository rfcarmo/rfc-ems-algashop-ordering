package com.algaworks.algashop.ordering.domain.model.customer;

import com.algaworks.algashop.ordering.domain.model.commons.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {

    public static final CustomerId DEFAULT_CUSTOMER_ID = new CustomerId();

    private CustomerTestDataBuilder() {
    }

    public static Customer.BrandNewCustomerBuilder brandNewCustomer() {
        return Customer.brandNew()
                .fullName(new FullName("John", "Doe"))
                .birthDate(new BirthDate(LocalDate.of(1983, 7, 16)))
                .email(new Email("johndoe@gmail.com"))
                .phone(new Phone("123456789"))
                .document(new Document("AB123456"))
                .promotionNotificationsAllowed(false)
                .address(Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build());
    }

    public static Customer.ExistingCustomerBuilder existingAnonymizedCustomer() {
        return Customer.existing()
                .id(DEFAULT_CUSTOMER_ID)
                .fullName(new FullName("Anonymous", "Customer"))
                .birthDate(null)
                .email(new Email("anonymus@anonymus.com"))
                .phone(new Phone("000000000"))
                .document(new Document("00000000"))
                .promotionNotificationsAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .address(Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build());
    }

    public static Customer.ExistingCustomerBuilder existingCustomer() {
        return Customer.existing()
                .id(DEFAULT_CUSTOMER_ID)
                .fullName(new FullName("Giulia", "Noel"))
                .birthDate(new BirthDate(LocalDate.of(1990, 9, 18)))
                .email(new Email("giulianoel@email.com"))
                .phone(new Phone("999888777"))
                .document(new Document("CD987654"))
                .promotionNotificationsAllowed(false)
                .archived(false)
                .registeredAt(OffsetDateTime.now().minusDays(1))
                .archivedAt(null)
                .loyaltyPoints(new LoyaltyPoints(20))
                .address(
                        Address.builder()
                                .street("Bourbon Street")
                                .complement("Apt. 114")
                                .neighborhood("North Ville")
                                .number("1133")
                                .city("York")
                                .state("South California")
                                .zipCode(new ZipCode("66667777"))
                                .build());
    }
}
