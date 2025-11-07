package com.algaworks.algashop.ordering.application.service.customer.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerUpdateInput;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder aCustomerUpdateInput() {
        return CustomerUpdateInput.builder()
                .firstName("Giulia")
                .lastName("Daye")
                .phone("999888777")
                .promotionalNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("777 Main St")
                        .number("123")
                        .complement("Apt 7")
                        .neighborhood("Downtown")
                        .city("Metropolis")
                        .state("NY")
                        .zipCode("11223344")
                        .build());
    }
}
