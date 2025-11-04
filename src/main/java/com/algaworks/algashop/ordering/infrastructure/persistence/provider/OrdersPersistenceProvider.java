package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public boolean exists(OrderId orderId) {
        return persistenceRepository.existsById(orderId.value().toLong());
    }

    @Override
    public Long count() {
        return persistenceRepository.count();
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Order aggregateRoot) {
        long orderId = aggregateRoot.id().value().toLong();

        persistenceRepository.findById(orderId)
                .ifPresentOrElse(
                        orderPersistenceEntity -> update(aggregateRoot, orderPersistenceEntity),
                        () -> insert(aggregateRoot));
    }

    @Override
    public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {
//        OffsetDateTime start = year.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
//        OffsetDateTime end = start.plusYears(1).minusNanos(1);
//
//        List<OrderPersistenceEntity> entities = persistenceRepository.placedByCustomerInYear(customerId.value(), start, end);

        List<OrderPersistenceEntity> entities = persistenceRepository.placedByCustomerInYear(customerId.value(), year.getValue());

        return entities.stream()
                .map(e -> disassembler.toDomainEntity(e))
                .toList();
    }

    @Override
    public Long salesQuantityByCustomerInYear(CustomerId customerId, Year year) {
        return persistenceRepository.salesQuantityByCustomerInYear(customerId.value(), year.getValue());
    }

    @Override
    public Money totalSoldForCustomer(CustomerId customerId) {
        return new Money(persistenceRepository.totalSoldByCustomer(customerId.value()));
    }

    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);

        persistenceRepository.saveAndFlush(persistenceEntity);

        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity orderPersistenceEntity) {
        OrderPersistenceEntity persistenceEntity = assembler.merge(orderPersistenceEntity, aggregateRoot);

        entityManager.detach(persistenceEntity);

        persistenceRepository.saveAndFlush(persistenceEntity);

        updateVersion(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {

        Field version = aggregateRoot.getClass().getDeclaredField("version");

        version.setAccessible(true);

        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());

        version.setAccessible(false);
    }

}
