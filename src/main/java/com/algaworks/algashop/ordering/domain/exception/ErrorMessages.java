package com.algaworks.algashop.ordering.domain.exception;

public class ErrorMessages {

    public static final String VALIDATION_ERROR_INVALID_EMAIL = "Invalid email format";

    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";

    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";

    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String ERROR_CUSTOMER_ALREADY_ARCHIVED = "Customer is already archived and cannot be modified";

    public static final String ERROR_LOYALTY_POINTS_ZERO_OR_NEGATIVE = "Loyalty points cannot be zero or negative";

    public static final String ERROR_STATUS_CANNOT_BE_CHANGED = "Order status cannot be changed from %s to %s for order with ID %s";

    public static final String ERROR_INVALID_SHIPPING_DELIVERY_DATE = "Expected delivery date %s is invalid for shipping method %s";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS = "Order %s cannot be placed because it has no items";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_NO_SHIPPING_INFO = "Order %s cannot be placed because it has no shipping information";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_NO_BILLING_INFO = "Order %s cannot be placed because it has no billing information";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_INVALID_SHIPPING_COST = "Order %s cannot be placed because it has an invalid shipping cost";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_INVALID_EXPECTED_DELIVERY_DATE = "Order %s cannot be placed because it has an invalid expected delivery date";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_NO_PAYMENT_METHOD = "Order %s cannot be placed because it has no payment method";

    public static final String ERROR_ORDER_DOES_NOT_CONTAIN_ORDER_ITEM = "Order with ID %s does not contain order item with ID %s";

    public static final String ERROR_PRODUCT_OUT_OF_STOCK = "Product with ID %s is out of stock";
}
