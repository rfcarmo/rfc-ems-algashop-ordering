package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BuyNowServiceTest {

    private final BuyNowService buyNowService = new BuyNowService();

    @Test
    public void givenValidProductAndDetails_whenBuyNow_shouldReturnPlacedOrder() {
        CustomerId customerId = new CustomerId();
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(1);
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customerId, billingInfo, shippingInfo, quantity, paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.id()).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(customerId);
        Assertions.assertThat(order.billing()).isEqualTo(billingInfo);
        Assertions.assertThat(order.shipping()).isEqualTo(shippingInfo);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.items().iterator().next().productId()).isEqualTo(product.productId());
        Assertions.assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        Assertions.assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity).add(shippingInfo.cost());
        Assertions.assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        Assertions.assertThat(order.totalItems()).isEqualTo(quantity);
    }

    @Test
    void givenOutOfStockProduct_whenBuyNow_shouldThrowProductOutOfStockException() {
        CustomerId customerId = new CustomerId();
        Product product = ProductTestDataBuilder.aUnavailableProduct().build();
        Quantity quantity = new Quantity(1);
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> buyNowService.buyNow(product, customerId, billingInfo, shippingInfo, quantity, paymentMethod));
    }

    @Test
    void givenInvalidQuantity_whenBuyNow_shouldThrowIllegalArgumentException() {
        CustomerId customerId = new CustomerId();
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(0);
        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> buyNowService.buyNow(product, customerId, billingInfo, shippingInfo, quantity, paymentMethod));
    }
}