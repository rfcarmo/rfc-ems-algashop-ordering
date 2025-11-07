package com.algaworks.algashop.ordering.domain.model.product;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Product(ProductId productId, ProductName name, Money price, Boolean inStock) {

    public Product {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        Objects.requireNonNull(inStock);
    }

    public void checkOutOfStock() {
        if (isOutOfStock()) {
            throw new ProductOutOfStockException(this.productId);
        }
    }

    private boolean isOutOfStock() {
        return Boolean.FALSE.equals(inStock());
    }
}
