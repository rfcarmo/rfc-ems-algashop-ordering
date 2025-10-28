package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();

    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

    private Money shippingCost = new Money("10.00");

    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);

    private ShippingInfo shippingInfo = aShippingInfo();

    private BillingInfo billingInfo = aBillingInfo();

    private boolean withItems = true;

    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() { }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.chageShippingInfo(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changeBillingInfo(billingInfo);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(ProductTestDataBuilder.aAltProductMemoryRam().build(), new Quantity(2));
            order.addItem(ProductTestDataBuilder.aAltProductSSD().build(), new Quantity(4));
        }

        switch (this.status) {
            case DRAFT -> {

            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
            }
            case CANCELED -> {
            }
        }

        return order;
    }

    public static ShippingInfo aShippingInfo() {
        return ShippingInfo.builder()
                .address(anAddress())
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();
    }

    public static BillingInfo aBillingInfo() {
        return BillingInfo.builder()
                .address(anAddress())
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("123 Main St")
                .number("Apt 4B")
                .neighborhood("Downtown")
                .complement("Near the park")
                .city("Anytown")
                .state("CA")
                .zipCode(new ZipCode("12345678"))
                .build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }


}
