package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.ProductTestDataBuilder;

import java.time.LocalDate;

import static com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class OrderTestDataBuilder {

    private CustomerId customerId = DEFAULT_CUSTOMER_ID;

    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

    private Shipping shipping = aShipping();

    private Billing billing = aBilling();

    private boolean withItems = true;

    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() { }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shipping);
        order.changeBillingInfo(billing);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(ProductTestDataBuilder.aAltProductMemoryRam().build(), new Quantity(4));
            order.addItem(ProductTestDataBuilder.aAltProductSSD().build(), new Quantity(2));
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
                order.place();
                order.markAsPaid();
                order.markAsReady();
            }
            case CANCELED -> {
                order.cancel();
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
    public static Shipping aAltFreeShipping() {
        return Shipping.builder()
                .address(anAltAddress())
                .recipient(Recipient.builder()
                        .document(new Document("000.111.222-33"))
                        .fullName(new FullName("Marie", "Jones"))
                        .phone(new Phone("(123) 456-9876"))
                        .build())
                .cost(Money.ZERO)
                .expectedDate(LocalDate.now().plusWeeks(2))
                .build();
    }


    public static Billing aBilling() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("123.456.789-00"))
                .fullName(new FullName("John", "Doe"))
                .phone(new Phone("(123) 456-7890"))
                .email(new Email("John@email.com"))
                .build();
    }

    public static Billing aAltBilling() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("999.777.888-11"))
                .fullName(new FullName("Carol", "Pie"))
                .phone(new Phone("(444) 123-12340"))
                .email(new Email("carol@email.com"))
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

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
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
