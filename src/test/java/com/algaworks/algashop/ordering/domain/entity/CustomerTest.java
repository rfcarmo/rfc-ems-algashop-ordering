package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_when_createCustomer_then_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(IdGenerator.generateTimeBasedUUID(),
                            "John Doe",
                            LocalDate.of(1983, 7, 16),
                            "invalid-email",
                            "123456789",
                            "AB123456",
                            false,
                            OffsetDateTime.now());
                });
    }

    @Test
    void given_invalidEmail_when_updateCustomerEmail_then_shouldThrowException() {
        Customer customer = new Customer(IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1983, 7, 16),
                "johndoe@gmail.com",
                "123456789",
                "AB123456",
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.changeEmail("invalid-email"));
    }

    @Test
    void given_unarchivedCustomer_when_archive_then_shouldAnonymize() {
        Customer customer = new Customer(IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1983, 7, 16),
                "johndoe@gmail.com",
                "123456789",
                "AB123456",
                false,
                OffsetDateTime.now());

        customer.archive();

        Assertions.assertWith(customer,
                c -> {
                    Assertions.assertThat(c.fullName()).isEqualTo("ANONYMOUS");
                    Assertions.assertThat(c.email()).isNotEqualTo("johndoe@gmail.com");
                    Assertions.assertThat(c.phone()).isEqualTo("000000000");
                    Assertions.assertThat(c.document()).isEqualTo("XXXXXXXX");
                    Assertions.assertThat(c.birthDate()).isNull();
                });
    }


}