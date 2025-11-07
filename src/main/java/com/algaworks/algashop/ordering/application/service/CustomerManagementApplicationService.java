package com.algaworks.algashop.ordering.application.service;

import com.algaworks.algashop.ordering.application.model.AddressData;
import com.algaworks.algashop.ordering.application.model.CustomerInput;
import com.algaworks.algashop.ordering.application.model.CustomerOutput;
import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

    private final CustomerRegistrationService customerRegistrationService;

    private final Customers customers;

    @Transactional
    public UUID create(CustomerInput customerInput) {
        Objects.requireNonNull(customerInput);

        AddressData addressData = customerInput.getAddress();

        Customer customer = customerRegistrationService.register(
                new FullName(customerInput.getFirstName(), customerInput.getLastName()),
                new BirthDate(customerInput.getBirthDate()),
                new Email(customerInput.getEmail()),
                new Phone(customerInput.getPhone()),
                new Document(customerInput.getDocument()),
                customerInput.isPromotionNotificationsAllowed(),
                Address.builder()
                        .street(addressData.getStreet())
                        .complement(addressData.getComplement())
                        .neighborhood(addressData.getNeighborhood())
                        .number(addressData.getNumber())
                        .city(addressData.getCity())
                        .state(addressData.getState())
                        .zipCode(new ZipCode(addressData.getZipCode()))
                        .build()

        );

        customers.add(customer);

        return customer.id().value();
    }

    @Transactional(readOnly = true)
    public CustomerOutput findById(UUID customerId) {
        Objects.requireNonNull(customerId);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(() -> new CustomerNotFoundException());

        return CustomerOutput.builder()
                .id(customer.id().value())
                .firstName(customer.fullName().firstName())
                .lastName(customer.fullName().lastName())
                .birthDate(customer.birthDate() != null ? customer.birthDate().value() : null)
                .email(customer.email().value())
                .phone(customer.phone().value())
                .document(customer.document().value())
                .promotionNotificationsAllowed(customer.isPromotionNotificationsAllowed())
                .loyaltyPoints(customer.loyaltyPoints().value())
                .registeredAt(customer.registeredAt())
                .archived(customer.isArchived())
                .archivedAt(customer.archivedAt() != null ? customer.archivedAt() : null)
                .address(AddressData.builder()
                        .street(customer.address().street())
                        .complement(customer.address().complement())
                        .neighborhood(customer.address().neighborhood())
                        .number(customer.address().number())
                        .city(customer.address().city())
                        .state(customer.address().state())
                        .zipCode(customer.address().zipCode().value())
                        .build())
                .build();
    }

}
