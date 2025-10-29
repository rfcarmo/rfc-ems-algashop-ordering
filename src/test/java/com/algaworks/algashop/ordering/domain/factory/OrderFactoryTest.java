package com.algaworks.algashop.ordering.domain.factory;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.factory.OrderFactory;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {

    @Test
    public void shouldGenerateFilledOrderThatCanBePlaced() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        Product product = ProductTestDataBuilder.aProduct().build();

        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        Quantity quantity = new Quantity(1);

        CustomerId customerId = new CustomerId();

        Order order = OrderFactory.filled(customerId, shipping, billing, paymentMethod, product, quantity);

        Assertions.assertWith(order, o -> {
            Assertions.assertThat(o.customerId()).isNotNull();
            Assertions.assertThat(o.shipping()).isEqualTo(shipping);
            Assertions.assertThat(o.billing()).isEqualTo(billing);
            Assertions.assertThat(o.paymentMethod()).isEqualTo(paymentMethod);
            Assertions.assertThat(o.items()).hasSize(1);
            Assertions.assertThat(o.isDraft()).isTrue();
        });

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();

    }

}