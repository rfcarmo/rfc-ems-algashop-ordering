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

}