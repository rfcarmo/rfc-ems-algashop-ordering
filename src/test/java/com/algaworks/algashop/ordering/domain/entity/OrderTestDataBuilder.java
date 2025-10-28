package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();

    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

    private Shipping shipping = aShipping();

    private BillingInfo billingInfo = aBillingInfo();

    private boolean withItems = true;

    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() { }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.chageShipping(shipping);
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

    public static Shipping aShipping() {
        return Shipping.builder()
                .address(anAddress())
                .recipient(Recipient.builder()
                        .document(new Document("123.456.789-00"))
                        .fullName(new FullName("John", "Doe"))
                        .phone(new Phone("(123) 456-7890"))
                        .build())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .build();
    }

    public static Shipping aAltShipping() {
        return Shipping.builder()
                .address(anAltAddress())
                .recipient(Recipient.builder()
                        .document(new Document("000.111.222-33"))
                        .fullName(new FullName("Marie", "Jones"))
                        .phone(new Phone("(123) 456-9876"))
                        .build())
                .cost(new Money("20.00"))
                .expectedDate(LocalDate.now().plusWeeks(2))
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

    public static Address anAltAddress() {
        return Address.builder()
                .street("456 Elm St")
                .number("Apt 9C")
                .neighborhood("Uptown")
                .complement("Next to the mall")
                .city("Othertown")
                .state("NY")
                .zipCode(new ZipCode("87654321"))
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

    public OrderTestDataBuilder shippingInfo(Shipping shipping) {
        this.shipping = shipping;
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
