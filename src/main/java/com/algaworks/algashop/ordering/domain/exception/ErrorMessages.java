package com.algaworks.algashop.ordering.domain.exception;

public class ErrorMessages {

    public static final String VALIDATION_ERROR_INVALID_EMAIL = "Invalid email format";

    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";

    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String ERROR_CUSTOMER_ALREADY_ARCHIVED = "Customer is already archived and cannot be modified";

    public static final String ERROR_LOYALTY_POINTS_ZERO_OR_NEGATIVE = "Loyalty points cannot be zero or negative";

}
