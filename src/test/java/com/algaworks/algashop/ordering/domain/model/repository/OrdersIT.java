package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@DataJpaTest
@Import({OrdersPersistenceProvider.class, OrderPersistenceEntityAssembler.class, OrderPersistenceEntityDisassembler.class})
class OrdersIT {

    private Orders orders;

    @Autowired
    public OrdersIT(Orders orders) {
        this.orders = orders;
    }

    @Test
    public void shouldPersistAndFind() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderId orderId = order.id();

        orders.add(order);
        Optional<Order> possibleOrder = orders.ofId(orderId);

        Assertions.assertThat(possibleOrder).isPresent();

        Order savedOrder = possibleOrder.get();

        Assertions.assertThat(savedOrder).satisfies(
                s -> {
                    Assertions.assertThat(s.id()).isEqualTo(order.id());
                    Assertions.assertThat(s.customerId()).isEqualTo(order.customerId());
                    Assertions.assertThat(s.totalAmount()).isEqualTo(order.totalAmount());
                    Assertions.assertThat(s.totalItems()).isEqualTo(order.totalItems());
                    Assertions.assertThat(s.placedAt()).isEqualTo(order.placedAt());
                    Assertions.assertThat(s.paidAt()).isEqualTo(order.paidAt());
                    Assertions.assertThat(s.cancelledAt()).isEqualTo(order.cancelledAt());
                    Assertions.assertThat(s.readyAt()).isEqualTo(order.readyAt());
                    Assertions.assertThat(s.status()).isEqualTo(order.status());
                    Assertions.assertThat(s.paymentMethod()).isEqualTo(order.paymentMethod());
                }
        );
    }

    @Test
    public void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(order.isPaid()).isTrue();
    }

    @Test
    public void shouldNotAllowStateUpdates() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Order order1 = orders.ofId(order.id()).orElseThrow();
        Order order2 = orders.ofId(order.id()).orElseThrow();

        order1.markAsPaid();
        orders.add(order1);

        order2.cancel();

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> orders.add(order2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(savedOrder.cancelledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();
    }

    @Test
    public void shouldCountExistingOrders() {
        Assertions.assertThat(orders.count()).isZero();

        Order order1 = OrderTestDataBuilder.anOrder().build();
        Order order2 = OrderTestDataBuilder.anOrder().build();

        orders.add(order1);
        orders.add(order2);

        Assertions.assertThat(orders.count()).isEqualTo(2L);
    }

    @Test
    public void shouldReturnIfOrderExists() {
        Order order = OrderTestDataBuilder.anOrder().build();

        orders.add(order);

        Assertions.assertThat(orders.exists(order.id())).isTrue();
        Assertions.assertThat(orders.exists(new OrderId())).isFalse();
    }
}