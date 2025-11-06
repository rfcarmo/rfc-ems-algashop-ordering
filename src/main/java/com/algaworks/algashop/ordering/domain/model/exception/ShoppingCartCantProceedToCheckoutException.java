package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {

    public ShoppingCartCantProceedToCheckoutException(ShoppingCartId id, String message) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_CANNOT_PROCEED_TO_CHECKOUT, id, message));
    }

}
