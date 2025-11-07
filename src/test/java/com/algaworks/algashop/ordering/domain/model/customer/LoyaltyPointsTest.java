package com.algaworks.algashop.ordering.domain.model.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void shouldGenerate() {
        LoyaltyPoints points = new LoyaltyPoints(10);

        Assertions.assertThat(points.value()).isEqualTo(10);
    }

    @Test
    void shouldAddValue() {
        LoyaltyPoints points = new LoyaltyPoints(10);
        LoyaltyPoints loyaltyPointsUpdated = points.add(5);

        Assertions.assertThat(loyaltyPointsUpdated.value()).isEqualTo(15);
    }

    @Test
    void shouldNotAddValue() {
        LoyaltyPoints points = new LoyaltyPoints(10);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> points.add(-5));

        Assertions.assertThat(points.value()).isEqualTo(10);
    }

    @Test
    void shouldNotAddZeroValue() {
        LoyaltyPoints points = new LoyaltyPoints(10);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> points.add(0));

        Assertions.assertThat(points.value()).isEqualTo(10);
    }

}