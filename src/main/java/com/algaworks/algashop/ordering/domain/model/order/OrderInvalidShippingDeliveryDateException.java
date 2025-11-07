package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.DomainException;

import java.time.LocalDate;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ERROR_INVALID_SHIPPING_DELIVERY_DATE;

public class OrderInvalidShippingDeliveryDateException extends DomainException {

    public OrderInvalidShippingDeliveryDateException(OrderId orderId, LocalDate expectedDeliveryDate) {
        super(String.format(ERROR_INVALID_SHIPPING_DELIVERY_DATE, expectedDeliveryDate, orderId));
    }
}
