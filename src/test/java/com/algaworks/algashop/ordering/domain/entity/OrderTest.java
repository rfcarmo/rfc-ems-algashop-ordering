package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.*;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerateDraftOrder() {
        CustomerId customerId = new CustomerId();

        Order order = Order.draft(customerId);

        Assertions.assertWith(order, o -> {
            Assertions.assertThat(o.id()).isNotNull();
            Assertions.assertThat(o.customerId()).isEqualTo(customerId);
            Assertions.assertThat(o.isDraft()).isTrue();
            Assertions.assertThat(o.items()).isEmpty();
            Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO);
            Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO);

            Assertions.assertThat(o.placedAt()).isNull();
            Assertions.assertThat(o.paidAt()).isNull();
            Assertions.assertThat(o.cancelledAt()).isNull();
            Assertions.assertThat(o.readyAt()).isNull();
            Assertions.assertThat(o.billing()).isNull();
            Assertions.assertThat(o.shipping()).isNull();
            Assertions.assertThat(o.paymentMethod()).isNull();
        });
    }

    @Test
    public void shouldAddItem() {
        Order order = Order.draft(new CustomerId());

        Product product = ProductTestDataBuilder.aProduct().build();

        ProductId productId = product.productId();

        order.addItem(product, new Quantity(2));

        Assertions.assertThat(order.items()).isNotEmpty();

        Assertions.assertThat(order.items()).size().isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem, i -> {
            Assertions.assertThat(i.id()).isNotNull();
            Assertions.assertThat(i.productId()).isEqualTo(productId);
            Assertions.assertThat(i.productName().value()).isEqualTo("Sample Product");
            Assertions.assertThat(i.price().value()).isEqualByComparingTo("10.00");
            Assertions.assertThat(i.quantity().value()).isEqualTo(2);
        });
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet() {
        Order order = Order.draft(new CustomerId());

        order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> items.clear());
    }

    @Test
    public void shouldCalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));

        order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(1));

        Assertions.assertThat(order.totalAmount().value()).isEqualTo("30.00");
        Assertions.assertThat(order.totalItems().value()).isEqualTo(3);
    }

    @Test
    public void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenPlaceAgain_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.place());
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_shouldChangePaymentMethod() {
        Order order = Order.draft(new CustomerId());

        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenPlacedOrder_whenPaid_shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        order.markAsPaid();

        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();
    }

    @Test
    public void givenDraftOrder_whenChangeBilling_shouldChangeBilling() {
        Billing billing = OrderTestDataBuilder.aBilling();

        Order order = Order.draft(new CustomerId());

        order.changeBillingInfo(billing);

        Assertions.assertThat(order.billing()).isEqualTo(billing);

    }

    @Test
    public void givenDraftOrder_whenChangeShipping_shouldChangeShipping() {
        Shipping shipping = OrderTestDataBuilder.aShipping();

        Order order = Order.draft(new CustomerId());

        order.changeShipping(shipping);

        Assertions.assertWith(order, o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping));

    }

    @Test
    public void givenDraftOrderAndDeliveryDateInThePast_whenChangeShippingInfo_shouldNotAllowChange() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(5);

        Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder().expectedDate(expectedDeliveryDate).build();

        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shipping));

    }

    @Test
    public void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order, o -> {
            Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("50.00"));
            Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5));
        });
    }

    @Test
    public void givenOutOffStockProduct_whenTryToAddToOrder_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(ProductTestDataBuilder
                .aUnavailableProduct().build(), new Quantity(1));

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    public void givenDraftOrder_whenChangeIt_shouldAllow() {
        Order order = Order.draft(new CustomerId());

        Product product = ProductTestDataBuilder.aProduct().build();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        order.changeShipping(shipping);
        order.changeBillingInfo(billing);
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        order.addItem(product, new Quantity(2));

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem, i -> {
            Assertions.assertThat(i.productId()).isEqualTo(product.productId());
            Assertions.assertThat(i.productName()).isEqualTo(product.productName());
            Assertions.assertThat(i.price()).isEqualTo(product.price());
            Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(2));
        });

    }

    @Test
    public void givenDraftOrder_whenChangeStatus_shouldNotAllowEdit() {
        Order order = Order.draft(new CustomerId());

        Product product = ProductTestDataBuilder.aProduct().build();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        order.changeShipping(shipping);
        order.changeBillingInfo(billing);
        order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);

        order.addItem(product, new Quantity(2));

        order.place();

        Product productAlt = ProductTestDataBuilder.aAltProductSSD().build();
        Shipping shippingAlt = OrderTestDataBuilder.aAltShipping();
        Billing billingAlt = OrderTestDataBuilder.aAltBilling();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeShipping(shippingAlt));

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeBillingInfo(billingAlt));

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD));

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.addItem(productAlt, new Quantity(1)));

    }

    @Test
    public void givenDraftOrder_shouldAllowRemoveItem() {
        Order order = Order.draft(new CustomerId());

        Product productA = ProductTestDataBuilder.aAltProductMemoryRam().build();
        Product productB = ProductTestDataBuilder.aAltProductSSD().build();

        order.addItem(productA, new Quantity(1));
        order.addItem(productB, new Quantity(1));

        Assertions.assertWith(order, o -> {
            Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("120.00"));
            Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(2));
        });

        OrderItem itemToRemove = order.items().stream()
                .filter(i -> i.productId().equals(productA.productId()))
                .findFirst()
                .orElseThrow();

        order.removeItem(itemToRemove.id());

        Assertions.assertWith(order, o -> {
            Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("70.00"));
            Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(1));
        });

    }

    @Test
    public void givenDraftOrder_whenTryToRemoveANonExistingItem_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        Product productA = ProductTestDataBuilder.aAltProductMemoryRam().build();
        Product productB = ProductTestDataBuilder.aAltProductSSD().build();

        order.addItem(productA, new Quantity(1));
        order.addItem(productB, new Quantity(1));

        Assertions.assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
                .isThrownBy(() -> order.removeItem(new OrderItemId()));

    }

    @Test
    public void givenANonDraftOrder_shouldNotAllowRemoveItem() {
        Order order = Order.draft(new CustomerId());

        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        Product productA = ProductTestDataBuilder.aAltProductMemoryRam().build();
        Product productB = ProductTestDataBuilder.aAltProductSSD().build();

        order.changeShipping(shipping);
        order.changeBillingInfo(billing);
        order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);

        order.addItem(productA, new Quantity(1));
        order.addItem(productB, new Quantity(1));

        order.place();

        OrderItem itemToRemove = order.items().stream()
                .filter(i -> i.productId().equals(productA.productId()))
                .findFirst()
                .orElseThrow();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(itemToRemove.id()));

    }

}