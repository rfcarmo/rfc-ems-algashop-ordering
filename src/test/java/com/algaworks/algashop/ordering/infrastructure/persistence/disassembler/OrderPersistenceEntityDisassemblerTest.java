package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.order.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityDisassembler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderPersistenceEntityDisassemblerTest {

    private final OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    @Test
    public void shouldConvertPersistenceEntityToDomainEntity() {
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        Order order = disassembler.toDomainEntity(orderPersistenceEntity);

        Assertions.assertThat(order).satisfies(s -> {
            Assertions.assertThat(s.id()).isEqualTo(new OrderId(orderPersistenceEntity.getId()));
            Assertions.assertThat(s.customerId()).isEqualTo(new CustomerId(orderPersistenceEntity.getCustomerId()));
            Assertions.assertThat(s.totalAmount()).isEqualTo(new Money(orderPersistenceEntity.getTotalAmount()));
            Assertions.assertThat(s.totalItems()).isEqualTo(new Quantity(orderPersistenceEntity.getTotalItems()));
            Assertions.assertThat(s.placedAt()).isEqualTo(orderPersistenceEntity.getPlacedAt());
            Assertions.assertThat(s.paidAt()).isEqualTo(orderPersistenceEntity.getPaidAt());
            Assertions.assertThat(s.canceledAt()).isEqualTo(orderPersistenceEntity.getCanceledAt());
            Assertions.assertThat(s.readyAt()).isEqualTo(orderPersistenceEntity.getReadyAt());
            Assertions.assertThat(s.status()).isEqualTo(OrderStatus.valueOf(orderPersistenceEntity.getStatus()));
            Assertions.assertThat(s.paymentMethod()).isEqualTo(PaymentMethod.valueOf(orderPersistenceEntity.getPaymentMethod()));
        });


    }

}