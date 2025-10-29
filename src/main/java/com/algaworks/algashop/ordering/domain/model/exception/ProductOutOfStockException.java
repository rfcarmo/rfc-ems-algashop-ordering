package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_PRODUCT_OUT_OF_STOCK;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId productId) {
        super(String.format(ERROR_PRODUCT_OUT_OF_STOCK, productId));
    }
}
