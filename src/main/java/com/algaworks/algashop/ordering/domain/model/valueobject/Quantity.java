package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.io.Serializable;
import java.util.Objects;

/**
 * Quantity
 * Regras de negócio
 *  - O valor deve ser zero ou positivo.
 *  - Multiplicações com Money só devem ser feitas quando quantity >= 1.
 *  - Deve expor a constante: Quantity.ZERO.
 *  - Implementar a interface Comparable para permitir comparação de instâncias de Quantity.
 * Construtor
 *  - Rejeita null;
 *  - Lança IllegalArgumentException se o valor for negativo.
 * Métodos esperados
 *  - Quantity add(Quantity other) - soma duas quantidades.
 *  - int compareTo(Quantity other) - compara duas quantidades.
 *  - String toString() - retorna o valor como texto.
 */

public record Quantity(Integer value) implements Serializable, Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value);

        if (value < 0) {
            throw new IllegalArgumentException(); // TODO: mensagem
        }
    }

    public Quantity add(Quantity quantity) {
        Objects.requireNonNull(quantity);

        return new Quantity(this.value + quantity.value);
    }

    @Override
    public int compareTo(Quantity quantity) {
        return this.value.compareTo(quantity.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
