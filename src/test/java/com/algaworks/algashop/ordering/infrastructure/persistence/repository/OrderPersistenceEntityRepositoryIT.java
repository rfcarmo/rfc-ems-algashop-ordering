package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository repository;

    private final CustomerPersistenceEntityRepository customerRepository;

    private CustomerPersistenceEntity customerPersistenceEntity;

    @Autowired
    OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository repository, CustomerPersistenceEntityRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    public void setup() {
        UUID customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID.value();

        if (!customerRepository.existsById(customerId)) {
            customerPersistenceEntity = customerRepository.saveAndFlush(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());
        }
    }

    @Test
    public void shouldPersist() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .customer(customerPersistenceEntity)
                .build();

        repository.saveAndFlush(entity);

        Assertions.assertThat(repository.existsById(entity.getId())).isTrue();

        OrderPersistenceEntity savedEntity = repository.findById(entity.getId()).orElseThrow();

        Assertions.assertThat(savedEntity.getItems()).isNotEmpty();
    }

    @Test
    public void shouldCount() {
        long ordersCount = repository.count();

        Assertions.assertThat(ordersCount).isZero();
    }

    @Test
    public void shouldSetAuditingValuesWhenSaving() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .customer(customerPersistenceEntity)
                .build();

        OrderPersistenceEntity savedEntity = repository.saveAndFlush(entity);

        Assertions.assertThat(savedEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(savedEntity.getLastModifiedByUserId()).isNotNull();
        Assertions.assertThat(savedEntity.getLastModifiedAt()).isNotNull();
    }

}