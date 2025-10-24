package com.algaworks.algashop.ordering.domain.valueobject;

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

public record BillingInfo(FullName fullName, Document document, Phone phone, Address address) {

    public BillingInfo {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
    }

}
