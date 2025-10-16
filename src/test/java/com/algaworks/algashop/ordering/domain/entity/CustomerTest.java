package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_when_createCustomer_then_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(
                            new CustomerId(),
                            new FullName("John", "Doe"),
                            LocalDate.of(1983, 7, 16),
                            "invalid-email",
                            "123456789",
                            "AB123456",
                            false,
                            OffsetDateTime.now(),
                            Address.builder()
                                    .street("123 Main St")
                                    .complement("Apt 101")
                                    .neighborhood("Downtown")
                                    .number("123")
                                    .city("Metropolis")
                                    .state("NY")
                                    .zipCode(new ZipCode("12345678"))
                                    .build()
                    );
                });
    }

    @Test
    void given_invalidEmail_when_updateCustomerEmail_then_shouldThrowException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                LocalDate.of(1983, 7, 16),
                "johndoe@gmail.com",
                "123456789",
                "AB123456",
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.changeEmail("invalid-email"));
    }

    @Test
    void given_unarchivedCustomer_when_archive_then_shouldAnonymize() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                LocalDate.of(1983, 7, 16),
                "johndoe@gmail.com",
                "123456789",
                "AB123456",
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build()
        );

        customer.archive();

        Assertions.assertWith(customer,
                c -> {
                    Assertions.assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Customer"));
                    Assertions.assertThat(c.email()).isNotEqualTo("johndoe@gmail.com");
                    Assertions.assertThat(c.phone()).isEqualTo("000000000");
                    Assertions.assertThat(c.document()).isEqualTo("XXXXXXXX");
                    Assertions.assertThat(c.birthDate()).isNull();
                    Assertions.assertThat(c.isPromotionNotificationsAllowed()).isFalse();
                    Assertions.assertThat(c.address()).isEqualTo(
                            Address.builder()
                                    .street("123 Main St")
                                    .complement(null)
                                    .neighborhood("Downtown")
                                    .number("Anonymized")
                                    .city("Metropolis")
                                    .state("NY")
                                    .zipCode(new ZipCode("12345678"))
                                    .build()
                    );
                });
    }

    @Test
    void given_archivedCustomer_when_tryToUpdate_then_shouldThrowException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Anonymous", "Customer"),
                null,
                "anonymus@anonymus.com",
                "000000000",
                "00000000",
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                LoyaltyPoints.ZERO,
                Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build()
        );

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("abcdef@hotmail.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("987654321"));
    }

    @Test
    void given_brandNewCustomer_when_addLoyaltyPoints_then_shouldSumPoints() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                LocalDate.of(1983, 7, 16),
                "johndoe@gmail.com",
                "123456789",
                "AB123456",
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build()
        );

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPoints().value()).isEqualTo(30);
    }

    @Test
    void given_brandNewCustomer_when_addInvalidLoyaltyPoints_then_shouldThrowException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                LocalDate.of(1983, 7, 16),
                "johndoe@gmail.com",
                "123456789",
                "AB123456",
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("123 Main St")
                        .complement("Apt 101")
                        .neighborhood("Downtown")
                        .number("123")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode(new ZipCode("12345678"))
                        .build()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(-1));
    }
}