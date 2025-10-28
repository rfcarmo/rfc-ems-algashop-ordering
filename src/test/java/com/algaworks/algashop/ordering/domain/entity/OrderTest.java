package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate() {
        Order draft = Order.draft(new CustomerId());
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
    public void givenDraftOrder_whenChangeBillingInfo_shouldChangeBillingInfo() {
        Address address = Address.builder()
                .street("123 Main St")
                .number("Apt 4B")
                .neighborhood("Downtown")
                .complement("Near the park")
                .city("Anytown")
                .state("CA")
                .zipCode(new ZipCode("12345678"))
                .build();


        BillingInfo billingInfo = BillingInfo.builder()
                .address(address)
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();

        BillingInfo expectedBillingInfo = BillingInfo.builder()
                .address(address)
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();

        Order order = Order.draft(new CustomerId());

        order.changeBillingInfo(billingInfo);

        Assertions.assertThat(order.billing()).isEqualTo(expectedBillingInfo);

    }

    @Test
    public void givenDraftOrder_whenChangeShippingInfo_shouldChangeShippingInfo() {
        Address address = Address.builder()
                .street("123 Main St")
                .number("Apt 4B")
                .neighborhood("Downtown")
                .complement("Near the park")
                .city("Anytown")
                .state("CA")
                .zipCode(new ZipCode("12345678"))
                .build();


        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();

        ShippingInfo expectedShippingInfo = ShippingInfo.builder()
                .address(address)
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = new Money("50.00");
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(5);

        order.chageShippingInfo(shippingInfo, shippingCost, expectedDeliveryDate);

        Assertions.assertWith(order, o -> {
            Assertions.assertThat(o.shipping()).isEqualTo(expectedShippingInfo);
            Assertions.assertThat(o.shippingCost().value()).isEqualByComparingTo("50.00");
            Assertions.assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate);
        });

    }

    @Test
    public void givenDraftOrderAndDeliveryDateInThePAst_whenChangeShippingInfo_shouldNotAllowChange() {
        Address address = Address.builder()
                .street("123 Main St")
                .number("Apt 4B")
                .neighborhood("Downtown")
                .complement("Near the park")
                .city("Anytown")
                .state("CA")
                .zipCode(new ZipCode("12345678"))
                .build();


        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = new Money("50.00");
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(5);

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.chageShippingInfo(shippingInfo, shippingCost, expectedDeliveryDate));

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
}