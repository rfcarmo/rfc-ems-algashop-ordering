package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.ERROR_PRODUCT_OUT_OF_STOCK;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId productId) {
        super(String.format(ERROR_PRODUCT_OUT_OF_STOCK, productId));
    }
}
