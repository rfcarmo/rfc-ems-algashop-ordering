package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Product(ProductId productId, ProductName productName, Money price, Boolean inStock) {

    public Product {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(productName);
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
