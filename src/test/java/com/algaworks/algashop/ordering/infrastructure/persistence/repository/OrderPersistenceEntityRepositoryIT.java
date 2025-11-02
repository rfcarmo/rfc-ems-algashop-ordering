package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository repository;

    @Autowired
    OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository repository) {
        this.repository = repository;
    }

    @Test
    public void shouldRun() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        repository.saveAndFlush(entity);

        Assertions.assertThat(repository.existsById(entity.getId())).isTrue();
    }

    @Test
    public void shouldCount() {
        long ordersCount = repository.count();

        Assertions.assertThat(ordersCount).isZero();
    }

}