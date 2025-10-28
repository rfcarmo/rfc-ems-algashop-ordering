package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.exception.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
public class Order {

    private OrderId id;

    private CustomerId customerId;

    private Money totalAmount;

    private Quantity totalItems;

    private OffsetDateTime placedAt;

    private OffsetDateTime paidAt;

    private OffsetDateTime cancelledAt;

    private OffsetDateTime readyAt;

    private BillingInfo billing;

    private ShippingInfo shipping;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private Money shippingCost;

    private LocalDate expectedDeliveryDate;

    private Set<OrderItem> items;

    public Order(OrderId id,
                 CustomerId customerId,
                 Money totalAmount,
                 Quantity totalItems,
                 OffsetDateTime placedAt,
                 OffsetDateTime paidAt,
                 OffsetDateTime cancelledAt,
                 OffsetDateTime readyAt,
                 BillingInfo billing,
                 ShippingInfo shipping,
                 OrderStatus status,
                 PaymentMethod paymentMethod,
                 Money shippingCost,
                 LocalDate expectedDeliveryDate,
                 Set<OrderItem> items) {

        setId(id);
        setCustomerId(customerId);
        setTotalAmount(totalAmount);
        setTotalItems(totalItems);
        setPlacedAt(placedAt);
        setPaidAt(paidAt);
        setCancelledAt(cancelledAt);
        setReadyAt(readyAt);
        setBilling(billing);
        setShipping(shipping);
        setStatus(status);
        setPaymentMethod(paymentMethod);
        setShippingCost(shippingCost);
        setExpectedDeliveryDate(expectedDeliveryDate);
        setItems(items);
    }

    public static Order draft(CustomerId customerId) {
        return new Order(
                new OrderId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                null,
                null,
                null,
                null,
                null,
                null,
                OrderStatus.DRAFT,
                null,
                Money.ZERO,
                null,
                new HashSet<>()
        );
    }

    public void addItem(ProductId productId, ProductName productName, Money price, Quantity quantity) {

        OrderItem orderItem = OrderItem.brandNew()
                .orderId(this.id())
                .price(price)
                .quantity(quantity)
                .productId(productId)
                .productName(productName)
                .build();

        if (this.items == null) {
            this.items = new HashSet<>();
        }

        this.items.add(orderItem);

        this.recalculateTotals();
    }

    public void place() {
        verifyIfCanChangeToPlaced();

        this.changeStatus(OrderStatus.PLACED);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void markAsPaid() {
        this.changeStatus(OrderStatus.PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod);

        this.setPaymentMethod(paymentMethod);
    }

    public void changeBillingInfo(BillingInfo billingInfo) {
        Objects.requireNonNull(billingInfo);

        this.setBilling(billingInfo);
    }

    public void chageShippingInfo(ShippingInfo shippingInfo, Money shippingCost, LocalDate expectedDeliveryDate) {
        Objects.requireNonNull(shippingInfo);
        Objects.requireNonNull(shippingCost);
        Objects.requireNonNull(expectedDeliveryDate);

        if (expectedDeliveryDate.isBefore(LocalDate.now())) {
            throw new OrderInvalidShippingDeliveryDateException(this.id(), expectedDeliveryDate);
        }

        this.setShipping(shippingInfo);
        this.setShippingCost(shippingCost);
        this.setExpectedDeliveryDate(expectedDeliveryDate);
    }

    public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
        Objects.requireNonNull(orderItemId);
        Objects.requireNonNull(quantity);

        OrderItem orderItem = this.findOrderItem(orderItemId);
        orderItem.changeQuantity(quantity);

        this.recalculateTotals();
    }

    public boolean isDraft() {
        return OrderStatus.DRAFT.equals(this.status());
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(this.status());
    }

    public boolean isPaid() {
        return OrderStatus.PAID.equals(this.status());
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime cancelledAt() {
        return cancelledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public BillingInfo billing() {
        return billing;
    }

    public ShippingInfo shipping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Money shippingCost() {
        return shippingCost;
    }

    public LocalDate expectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(this.items);
    }

    private OrderItem findOrderItem(OrderItemId orderItemId) {
        Objects.requireNonNull(orderItemId);

        return this.items.stream()
                .filter(i -> i.id().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
    }

    private void recalculateTotals() {
        BigDecimal totalItemsAmount = this.items.stream()
                .map(i -> i.totalAmount().value())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        Integer totalItemsQuantity = this.items.stream()
                .map(i -> i.quantity().value())
                .reduce(0, (a, b) -> a + b);

        BigDecimal shippingCost;

        if (this.shippingCost() == null) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = this.shippingCost.value();
        }

        BigDecimal totalAmount = totalItemsAmount.add(shippingCost);

        this.setTotalAmount(new Money(totalAmount));
        this.setTotalItems(new Quantity(totalItemsQuantity));
    }

    private void changeStatus(OrderStatus newOrderStatus) {
        Objects.requireNonNull(newOrderStatus);

        if (this.status().canNotChangeTo(newOrderStatus)) {
            throw new OrderStatusCannotBeChangedException(this.id(), this.status(), newOrderStatus);
        }

        this.setStatus(newOrderStatus);
    }

    private void verifyIfCanChangeToPlaced() {
        if (this.items == null || this.items.isEmpty()) {
            throw OrderCannotBePlacedException.noItems(this.id());
        }

        if (this.shipping() == null) {
            throw OrderCannotBePlacedException.noShippingInfo(this.id());
        }

        if (this.billing() == null) {
            throw OrderCannotBePlacedException.noBillingInfo(this.id());
        }

        if (this.shippingCost() == null || this.shippingCost().value().compareTo(BigDecimal.ZERO) < 0) {
            throw OrderCannotBePlacedException.invalidShippingCost(this.id());
        }

        if (this.expectedDeliveryDate() == null || this.expectedDeliveryDate().isBefore(LocalDate.now())) {
            throw OrderCannotBePlacedException.invalidExpectedDeliveryDate(this.id());
        }

        if (this.paymentMethod() == null) {
            throw OrderCannotBePlacedException.noPaymentMethod(this.id());
        }
    }

    private void setId(OrderId id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);

        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);

        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);

        this.totalItems = totalItems;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setCancelledAt(OffsetDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setBilling(BillingInfo billing) {
        this.billing = billing;
    }

    private void setShipping(ShippingInfo shipping) {
        this.shipping = shipping;
    }

    private void setStatus(OrderStatus status) {
        Objects.requireNonNull(status);

        this.status = status;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setShippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
    }

    private void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    private void setItems(Set<OrderItem> items) {
        Objects.requireNonNull(items);

        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
