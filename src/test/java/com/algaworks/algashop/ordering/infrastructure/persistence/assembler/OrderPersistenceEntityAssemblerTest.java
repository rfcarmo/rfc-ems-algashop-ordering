package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    public void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);

        Assertions.assertThat(orderPersistenceEntity).satisfies(
                p -> {
                    Assertions.assertThat(p.getId()).isEqualTo(order.id().value().toLong());
                    Assertions.assertThat(p.getCustomerId()).isEqualTo(order.customerId().value());
                    Assertions.assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value());
                    Assertions.assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value());
                    Assertions.assertThat(p.getStatus()).isEqualTo(order.status().name());
                    Assertions.assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name());
                    Assertions.assertThat(p.getPlacedAt()).isEqualTo(order.placedAt());
                    Assertions.assertThat(p.getPaidAt()).isEqualTo(order.paidAt());
                    Assertions.assertThat(p.getCancelledAt()).isEqualTo(order.cancelledAt());
                    Assertions.assertThat(p.getReadyAt()).isEqualTo(order.readyAt());
                }
        );
    }

    @Test
    public void ShouldMerge() {

    }

}