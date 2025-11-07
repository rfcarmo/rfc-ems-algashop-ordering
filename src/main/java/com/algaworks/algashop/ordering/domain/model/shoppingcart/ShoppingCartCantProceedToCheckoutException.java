package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.DomainException;
import com.algaworks.algashop.ordering.domain.model.ErrorMessages;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {

    public ShoppingCartCantProceedToCheckoutException(ShoppingCartId id, String message) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_CANNOT_PROCEED_TO_CHECKOUT, id, message));
    }

}
