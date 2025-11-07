package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.IdGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class OrderPersistenceEntityTestDataBuilder {

    public OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntity.OrderPersistenceEntityBuilder existingOrder() {
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                .totalItems(2)
                .totalAmount(new BigDecimal(600))
                .status("PLACED")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .items(Set.of(existingOrderItem().build(), existingAltOrderItem().build()));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingOrderItem() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTimeBasedUUID())
                .productName("Product 1")
                .quantity(1)
                .price(new BigDecimal(500))
                .totalAmount(new BigDecimal(500));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingAltOrderItem() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTimeBasedUUID())
                .productName("Product 2")
                .quantity(1)
                .price(new BigDecimal(100))
                .totalAmount(new BigDecimal(100));
    }
}
