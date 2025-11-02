package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceRepository;

    private final OrderPersistenceEntityAssembler assembler;

    private final OrderPersistenceEntityDisassembler disassembler;

    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> persistenceEntity = persistenceRepository.findById(orderId.value().toLong());

        return persistenceEntity.map(e -> disassembler.toDomainEntity(e));
    }

    @Override
    public boolean existis(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order aggregateRoot) {
        long orderId = aggregateRoot.id().value().toLong();

        persistenceRepository.findById(orderId)
                .ifPresentOrElse(
                        orderPersistenceEntity -> update(aggregateRoot, orderPersistenceEntity),
                        () -> insert(aggregateRoot));
    }

    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);

        persistenceRepository.saveAndFlush(persistenceEntity);

        aggregateRoot.setVersion(persistenceEntity.getVersion());
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity orderPersistenceEntity) {
        OrderPersistenceEntity persistenceEntity = assembler.merge(orderPersistenceEntity, aggregateRoot);

        entityManager.detach(persistenceEntity);

        persistenceRepository.saveAndFlush(persistenceEntity);

        aggregateRoot.setVersion(persistenceEntity.getVersion());
    }

    @Override
    public int count() {
        return 0;
    }
}
