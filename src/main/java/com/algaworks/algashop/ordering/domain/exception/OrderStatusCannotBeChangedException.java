package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.ERROR_STATUS_CANNOT_BE_CHANGED;

public class OrderStatusCannotBeChangedException extends DomainException {

    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus newOrderStatus) {
        super(String.format(ERROR_STATUS_CANNOT_BE_CHANGED, status, newOrderStatus, id));
    }
}
