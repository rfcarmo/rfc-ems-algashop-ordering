package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints> {

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints(Integer value) {
        Objects.requireNonNull(value);

        if (value < 0) {
            throw new IllegalArgumentException("Loyalty points cannot be negative");
        }

        this.value = value;
    }

    public LoyaltyPoints add(Integer newPoints) {
        return add(new LoyaltyPoints(newPoints));
    }

    public LoyaltyPoints add(LoyaltyPoints newPoints) {
        Objects.requireNonNull(newPoints);

        if (newPoints.value() < 0) {
            throw new IllegalArgumentException("Loyalty points to add cannot be negative");
        }

        return new LoyaltyPoints(this.value() + newPoints.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(LoyaltyPoints o) {
        return this.value().compareTo(o.value());
    }
}
