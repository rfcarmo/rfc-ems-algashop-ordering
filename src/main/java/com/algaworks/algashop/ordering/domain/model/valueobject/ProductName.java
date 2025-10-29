package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.validator.FieldValidations;

/**
 * ProductName
 * Regras de negócio
 *  - O valor deve ser uma String não nula e não pode estar em branco.
 * Construtor
 *  - Rejeita null;
 *  - Lança IllegalArgumentException se for em branco.
 * Método esperado
 *  - String toString() - retorna o valor diretamente.
 */

public record ProductName(String value) {

    public ProductName {
        FieldValidations.requiresNonBlank(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
