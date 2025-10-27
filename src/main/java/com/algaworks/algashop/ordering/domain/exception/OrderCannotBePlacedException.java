package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED;

public class OrderCannotBePlacedException extends DomainException {

    public OrderCannotBePlacedException(OrderId orderId) {
        super(String.format(ERROR_ORDER_CANNOT_BE_PLACED, orderId));
    }
}
