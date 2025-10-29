package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void given_invalidEmail_when_createCustomer_then_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CustomerTestDataBuilder.brandNewCustomer().email(new Email("invalid-email")).build());
    }

    @Test
    void given_invalidEmail_when_updateCustomerEmail_then_shouldThrowException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("invalid-email")));
    }

    @Test
    void given_unarchivedCustomer_when_archive_then_shouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        customer.archive();

        Assertions.assertWith(customer,
                c -> {
                    Assertions.assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Customer"));
                    Assertions.assertThat(c.email()).isNotEqualTo(new Email("johndoe@gmail.com"));
                    Assertions.assertThat(c.phone()).isEqualTo(new Phone("000000000"));
                    Assertions.assertThat(c.document()).isEqualTo(new Document("XXXXXXXX"));
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
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomer().build();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("abcdef@hotmail.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("987654321")));
    }

    @Test
    void given_brandNew_when_addLoyaltyPoints_then_shouldSumPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPoints().value()).isEqualTo(30);
    }

    @Test
    void given_brandNew_when_addInvalidLoyaltyPoints_then_shouldThrowException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(-1));
    }
}