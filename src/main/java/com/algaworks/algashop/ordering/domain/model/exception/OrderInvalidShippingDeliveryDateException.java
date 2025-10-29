package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import java.time.LocalDate;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_INVALID_SHIPPING_DELIVERY_DATE;

public class OrderInvalidShippingDeliveryDateException extends DomainException {

    public OrderInvalidShippingDeliveryDateException(OrderId orderId, LocalDate expectedDeliveryDate) {
        super(String.format(ERROR_INVALID_SHIPPING_DELIVERY_DATE, expectedDeliveryDate, orderId));
    }
}
