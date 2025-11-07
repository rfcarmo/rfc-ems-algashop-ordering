package com.algaworks.algashop.ordering.application.service.customer.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;

import java.time.LocalDate;

public class CustomerInpuTestDataBuilder {

    public static CustomerInput.CustomerInputBuilder aCustomerInput() {
        return CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2001, 1, 1))
                .email("john@email.com")
                .phone("123456789")
                .document("12345678")
                .promotionalNotificationsAllowed(false)
                .address(AddressData.builder()
                        .street("123 Main St")
                        .number("100")
                        .complement("Apt 1")
                        .neighborhood("Downtown")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode("12345678")
                        .build());
    }
}
