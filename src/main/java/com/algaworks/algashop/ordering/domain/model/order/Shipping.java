package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import lombok.Builder;

import java.time.LocalDate;
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
public record Shipping(Recipient recipient, Address address, Money cost, LocalDate expectedDate) {

    public Shipping {
        Objects.requireNonNull(recipient);
        Objects.requireNonNull(address);
        Objects.requireNonNull(cost);
        Objects.requireNonNull(expectedDate);
    }

}
