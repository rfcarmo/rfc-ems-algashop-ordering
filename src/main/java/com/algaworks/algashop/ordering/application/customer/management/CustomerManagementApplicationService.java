package com.algaworks.algashop.ordering.application.customer.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.utility.Mapper;
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

    private final Mapper mapper;

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
                customerInput.getPromotionalNotificationsAllowed(),
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

        return mapper.convert(customer, CustomerOutput.class);
    }

    @Transactional
    public void update(UUID customerId, CustomerUpdateInput input) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(input);

        Customer customer = customers.ofId(new CustomerId(customerId)).orElseThrow(() -> new CustomerNotFoundException());

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changePhone(new Phone(input.getPhone()));

        if (Boolean.TRUE.equals(input.getPromotionalNotificationsAllowed())) {
            customer.enablePromotionNotifications();
        } else {
            customer.disablePromotionNotifications();
        }

        AddressData addressData = input.getAddress();

        customer.changeAddress(Address.builder()
                .street(addressData.getStreet())
                .complement(addressData.getComplement())
                .neighborhood(addressData.getNeighborhood())
                .number(addressData.getNumber())
                .city(addressData.getCity())
                .state(addressData.getState())
                .zipCode(new ZipCode(addressData.getZipCode()))
                .build());

        customers.add(customer);
    }

}
