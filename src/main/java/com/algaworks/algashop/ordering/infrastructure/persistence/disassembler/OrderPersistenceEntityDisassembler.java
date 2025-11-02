package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity orderPersistenceEntity) {
        return Order.existing()
                .id(new OrderId(orderPersistenceEntity.getId()))
                .customerId(new CustomerId(orderPersistenceEntity.getCustomerId()))
                .totalAmount(new Money(orderPersistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(orderPersistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(orderPersistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(orderPersistenceEntity.getPaymentMethod()))
                .placedAt(orderPersistenceEntity.getPlacedAt())
                .paidAt(orderPersistenceEntity.getPaidAt())
                .cancelledAt(orderPersistenceEntity.getCancelledAt())
                .readyAt(orderPersistenceEntity.getReadyAt())
                .items(new HashSet<>())
                .build();
    }

}
