package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerateBrandNewOrderItem() {
        Product product = ProductTestDataBuilder.aProduct().build();
        OrderId orderId = new OrderId();
        Quantity quantity = new Quantity(1);

        OrderItem orderItem =OrderItem.brandNew()
                .product(product)
                .quantity(quantity)
                .orderId(orderId)
                .build();

        Assertions.assertWith(orderItem, o -> {
            Assertions.assertThat(o.id()).isNotNull();
            Assertions.assertThat(o.orderId()).isEqualTo(orderId);
            Assertions.assertThat(o.productId()).isEqualTo(product.productId());
            Assertions.assertThat(o.productName()).isEqualTo(product.productName());
            Assertions.assertThat(o.price()).isEqualTo(product.price());
            Assertions.assertThat(o.quantity()).isEqualTo(quantity);
        });
    }

}