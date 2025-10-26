package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate() {
        Order draft = Order.draft(new CustomerId());
    }

    @Test
    public void shouldAddItem() {
        Order order = Order.draft(new CustomerId());

        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Product Name"),
                new Money("1000.00"),
                new Quantity(2)
        );

        Assertions.assertThat(order.items()).isNotEmpty();

        Assertions.assertThat(order.items()).size().isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem, i -> {
            Assertions.assertThat(i.id()).isNotNull();
            Assertions.assertThat(i.productId()).isEqualTo(productId);
            Assertions.assertThat(i.productName().value()).isEqualTo("Product Name");
            Assertions.assertThat(i.price().value()).isEqualByComparingTo("1000.00");
            Assertions.assertThat(i.quantity().value()).isEqualTo(2);
        });
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet() {
        Order order = Order.draft(new CustomerId());

        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Product Name"),
                new Money("1000.00"),
                new Quantity(2)
        );

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> items.clear());
    }

}