package com.algaworks.algashop.ordering.domain.model.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldValidations {

    private FieldValidations() {
    }

    public static void requiresNonBlank(String value) {
        requiresNonBlank(value, null);
    }

    public static void requiresNonBlank(String value, String message) {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requiresValidEmail(String email) {
        requiresValidEmail(email, null);
    }

    public static void requiresValidEmail(String email, String errorMessage) {
        Objects.requireNonNull(email);

        if (email.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requiresValidQuantity(int quantity) {
        Objects.requireNonNull(quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
