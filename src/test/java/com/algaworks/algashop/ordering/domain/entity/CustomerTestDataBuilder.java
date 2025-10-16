package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {

    private CustomerTestDataBuilder() {
    }

    public static Customer.BrandNewCustomerBuilder brandNewCustomer() {
        return Customer.brandNew()
                .fullName(new FullName("John", "Doe"))
                .birthDate(LocalDate.of(1983, 7, 16))
                .email("johndoe@gmail.com")
                .phone("123456789")
                .document("AB123456 ")
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
                .id(new CustomerId())
                .fullName(new FullName("Anonymous", "Customer"))
                .birthDate(null)
                .email("anonymus@anonymus.com")
                .phone("000000000")
                .document("00000000")
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
}
