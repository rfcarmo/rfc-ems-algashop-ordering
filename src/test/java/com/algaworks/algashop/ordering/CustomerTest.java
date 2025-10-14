package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTest {

    @Test
    public void testCustomerCreation() {
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1983, 07, 16),
                "test@test.com",
                "123456789",
                "123.456.789-00",
                true,
                OffsetDateTime.now());

        customer.addLoyaltyPoints(10);

    }

}
