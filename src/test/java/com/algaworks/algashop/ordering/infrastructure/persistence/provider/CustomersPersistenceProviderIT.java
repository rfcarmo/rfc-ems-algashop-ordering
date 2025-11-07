package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class, CustomerPersistenceEntityAssembler.class, CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class CustomersPersistenceProviderIT {

    private CustomersPersistenceProvider customersPersistenceProvider;

    private CustomerPersistenceEntityRepository repository;

    @Autowired
    CustomersPersistenceProviderIT(CustomersPersistenceProvider customersPersistenceProvider,
                                   CustomerPersistenceEntityRepository repository) {
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.repository = repository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        String originEmail = "user@email.com";

        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .email(new Email(originEmail))
                .build();

        UUID customerId = customer.id().value();

        customersPersistenceProvider.add(customer);

        CustomerPersistenceEntity customerPersistence = repository.findById(customerId).orElseThrow();

        Assertions.assertThat(customerPersistence).satisfies(
                cp -> {
                    Assertions.assertThat(cp.getEmail()).isEqualTo(originEmail);

                    Assertions.assertThat(cp.getCreatedByUserId()).isNotNull();
                    Assertions.assertThat(cp.getLastModifiedByUserId()).isNotNull();
                    Assertions.assertThat(cp.getLastModifiedAt()).isNotNull();
                }

        );

        String newEmail = "new@email.com";

        customer = customersPersistenceProvider.ofId(customer.id()).orElseThrow();
        customer.changeEmail(new Email(newEmail));
        customersPersistenceProvider.add(customer);

        customerPersistence = repository.findById(customerId).orElseThrow();

        Assertions.assertThat(customerPersistence).satisfies(
                cp -> {
                    Assertions.assertThat(cp.getEmail()).isEqualTo(newEmail);
                    Assertions.assertThat(cp.getCreatedByUserId()).isNotNull();
                    Assertions.assertThat(cp.getLastModifiedByUserId()).isNotNull();
                    Assertions.assertThat(cp.getLastModifiedAt()).isNotNull();
                }
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddAndFindNotFailWhenNoTransaction() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        customersPersistenceProvider.add(customer);

        Assertions.assertThatNoException().isThrownBy(() -> customersPersistenceProvider.ofId(customer.id()).orElseThrow());
    }

    @Test
    void shouldCountCorrectly() {
        Assertions.assertThat(customersPersistenceProvider.count()).isZero();

        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customersPersistenceProvider.add(customer);

        Assertions.assertThat(customersPersistenceProvider.count()).isEqualTo(1L);
    }

    @Test
    void shouldVerifyIfExists() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerId customerId = customer.id();

        Assertions.assertThat(customersPersistenceProvider.exists(customerId)).isFalse();

        customersPersistenceProvider.add(customer);

        Assertions.assertThat(customersPersistenceProvider.exists(customerId)).isTrue();
    }

}