package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Money
 * Regras de negócio
 *  - O valor nunca pode ser nulo ou negativo.
 *  - Deve sempre ser armazenado com escala 2 e arredondamento HALF_EVEN.
 *  - Deve expor a constante: Money.ZERO.
 *  - Implementar a interface Comparable para permitir comparação de instâncias de Money.
 * Construtores
 *  - Money(String value)
 *  - Money(BigDecimal value)
 * Ambos os construtores devem:
 *  - Rejeitar null;
 *  - Ajustar a escala para 2 com HALF_EVEN;
 *  - Lançar IllegalArgumentException se o valor for negativo.
 * Métodos esperados
 *  - Money multiply(Quantity quantity) - lança exceção se quantity < 1. Retorna um novo Money.
 *  - Money add(Money other) - soma dois valores monetários.
 *  - Money divide(Money other) - divide os valores, mantendo HALF_EVEN.
 *  - int compareTo(Money other) - compara dois valores monetários.
 *  - String toString() - retorna o valor formatado.
 */

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);

        this.value = value.setScale(2, ROUNDING_MODE);

        if (this.value.signum() == -1) {
            throw new IllegalArgumentException(); // TODO: mensagem
        }
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);

        if (quantity.value() < 1) {
            throw new IllegalArgumentException(); // TODO: mensagem
        }

        BigDecimal multiplication = this.value.multiply(BigDecimal.valueOf(quantity.value()));

        return new Money(multiplication);
    }

    public Money add(Money money) {
        Objects.requireNonNull(money);

        return new Money(this.value.add(money.value));
    }

    public Money divide(Money money) {
        return new Money(this.value.divide(money.value, ROUNDING_MODE));
    }

    @Override
    public int compareTo(Money money) {
        return this.value.compareTo(money.value);
    }

    @Override
    public String toString() {
        return value().toString();
    }
}
