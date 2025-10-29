package com.algaworks.algashop.ordering.domain.model.valueobject;

import lombok.Builder;

import java.util.Objects;

/**
 * BillingInfo
 * Regras de negócio
 *  - Todos os campos são obrigatórios:
 *      - FullName fullName
 *      - Document document
 *      - Phone phone
 *      - Address address
 * Implementação
 *  - Utilize record com anotação @Builder.
 *  - No construtor compacto, valide todos os campos com Objects.requireNonNull().
 */

@Builder(toBuilder = true)
public record Billing(FullName fullName, Document document, Phone phone, Email email, Address address) {

    @Builder(toBuilder = true)
    public Billing {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(email);
        Objects.requireNonNull(address);
    }

}
