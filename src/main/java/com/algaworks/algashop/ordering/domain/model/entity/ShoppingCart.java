package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainItemException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainProductException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private OffsetDateTime createdAt;
    private Set<ShoppingCartItem> items;
    private Long version;

    /**
     * builderClassName = "ExistingShoppingCartBuilder": o nome da classe interna gerada será ExistingShoppingCartBuilder.
     * builderMethodName = "existing": o metodo estático para obter o builder será existing() (em vez do padrão builder()).
     */
    @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
    public ShoppingCart(ShoppingCartId id, CustomerId customerId, Money totalAmount, Quantity totalItems,
                           OffsetDateTime createdAt, Set<ShoppingCartItem> items, Long version) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
        this.items = items;
        this.version = version;
    }

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(new ShoppingCartId(), customerId, Money.ZERO, Quantity.ZERO, OffsetDateTime.now(),
                new HashSet<>(), null);
    }

    public void empty() {
        this.items.clear();
        this.setTotalAmount(Money.ZERO);
        this.setTotalItems(Quantity.ZERO);
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);

        product.checkOutOfStock();

        this.searchItemByProductId(product.productId())
                .ifPresentOrElse(i -> updateItem(i, product, quantity),
                                 () -> insertItem(product, quantity));

        this.recalculateTotals();
    }

    private void insertItem(Product product, Quantity quantity) {
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(this.id())
                .productId(product.productId())
                .productName(product.name())
                .price(product.price())
                .quantity(quantity)
                .available(product.inStock())
                .totalAmount(product.price().multiply(quantity))
                .build();

        this.items.add(shoppingCartItem);
    }

    private void updateItem(ShoppingCartItem shoppingCartItem, Product product, Quantity quantity) {
        shoppingCartItem.refreshProduct(product);
        shoppingCartItem.changeQuantity(shoppingCartItem.quantity().add(quantity));
    }

    public void removeItem(ShoppingCartItemId shoppingCartItemId) {
        ShoppingCartItem item = findItem(shoppingCartItemId);
        this.items.remove(item);
        this.recalculateTotals();
    }

    public void refreshItem(Product product) {
        ShoppingCartItem item = findItem(product.productId());
        item.refreshProduct(product);
        this.recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        ShoppingCartItem item = findItem(shoppingCartItemId);
        item.changeQuantity(quantity);
        this.recalculateTotals();
    }

    private Optional<ShoppingCartItem> searchItemByProductId(ProductId productId) {
        Objects.requireNonNull(productId);

        return this.items.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst();
    }

    public ShoppingCartItem findItem(ProductId productIdId) {
        return this.searchItemByProductId(productIdId)
                .orElseThrow(() -> new ShoppingCartDoesNotContainProductException(this.id(), productIdId));
    }

    public ShoppingCartItem findItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId);

        return this.items.stream()
                .filter(i -> i.id().equals(shoppingCartItemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), shoppingCartItemId));
    }

    public void recalculateTotals() {
        BigDecimal totalValue = this.items.stream()
                .map(i -> i.totalAmount().value())
                .reduce(BigDecimal.ZERO, (acum, val) -> acum.add(val));

        Integer totalQuantity = this.items.stream()
                .map(i -> i.quantity().value())
                .reduce(0, (acum, val) -> Integer.sum(acum, val));

        this.setTotalAmount(new Money(totalValue));
        this.setTotalItems(new Quantity(totalQuantity));
    }

    public boolean containsUnavailableItems() {
        return this.items.stream().anyMatch(i -> !i.isAvailable());
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ShoppingCartId id() {
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

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public Set<ShoppingCartItem> items() {
        return Collections.unmodifiableSet(items);
    }

    public Long version() {
        return version;
    }

    private void setId(ShoppingCartId id) {
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

    private void setCreatedAt(OffsetDateTime createdAt) {
        Objects.requireNonNull(createdAt);
        this.createdAt = createdAt;
    }

    private void setItems(Set<ShoppingCartItem> items) {
        Objects.requireNonNull(items);
        this.items = Collections.unmodifiableSet(items);
    }

    private void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
