package com.algaworks.algashop.ordering.application.service;

import com.algaworks.algashop.ordering.application.model.AddressData;
import com.algaworks.algashop.ordering.application.model.CustomerInput;
import com.algaworks.algashop.ordering.application.model.CustomerOutput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Test
    public void shouldRegister() {
        CustomerInput customerInput = CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2001, 1, 1))
                .email("john@email.com")
                .phone("123456789")
                .document("12345678")
                .promotionNotificationsAllowed(false)
                .address(AddressData.builder()
                        .street("123 Main St")
                        .number("100")
                        .complement("Apt 1")
                        .neighborhood("Downtown")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode("12345678")
                        .build())
                .build();

        UUID customerId = customerManagementApplicationService.create(customerInput);

        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
        Assertions.assertThat(customerOutput.getFirstName()).isEqualTo("John");
        Assertions.assertThat(customerOutput.getLastName()).isEqualTo("Doe");
    }

}