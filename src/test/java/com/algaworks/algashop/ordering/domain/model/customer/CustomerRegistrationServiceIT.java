package com.algaworks.algashop.ordering.domain.model.customer;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class CustomerRegistrationServiceIT {

    @Autowired
    private CustomerRegistrationService customerRegistrationService;

    @Test
    public void shouldRegisterCustomer() {
        Customer customer = customerRegistrationService.register(
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1983, 7, 16)),
                new Email("test@email.com"),
                new Phone("119999999"),
                new Document("kk123456"),
                true,
                Address.builder()
                        .street("Street ABC")
                        .number("123")
                        .complement("Apt 45")
                        .neighborhood("Neighborhood XYZ")
                        .city("City QWE")
                        .state("ST")
                        .zipCode(new ZipCode("12345678"))
                        .build()
        );

        Assertions.assertThat(customer.fullName()).isEqualTo(new FullName("John", "Doe"));
        Assertions.assertThat(customer.email()).isEqualTo(new Email("test@email.com"));
    }

}