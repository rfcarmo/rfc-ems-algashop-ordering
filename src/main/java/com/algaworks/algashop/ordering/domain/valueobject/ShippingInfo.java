package com.algaworks.algashop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

/**
 * ShippingInfo
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
public record ShippingInfo(FullName fullName, Document document, Phone phone, Address address) {

    public ShippingInfo {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
    }

}
