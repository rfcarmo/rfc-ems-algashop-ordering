package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

    /**
     * Construtor canônico compacto (o bloco public ShoppingCartId { ... }):
     *     *
     * É o construtor canônico do record — executado sempre que qualquer outro construtor delega para o construtor principal.
     * Serve para validações/invariantes do componente (aqui Objects.requireNonNull(value)), garantindo que t\odo ShoppingCartId
     * criado esteja válido, independentemente de qual outro construtor foi usado.
     *
     * Construtor sem-args (public ShoppingCartId()):
     *
     * É um construtor de conveniência que cria um ShoppingCartId novo usando IdGenerator.generateTimeBasedUUID().
     * Ele delega para o construtor canônico (via this(...)), portanto a validação do canônico é aplicada automaticamente.
     *
     * Por que precisa dos dois:
     *
     * O canônico garante a invariância do record (não importa como a instância foi criada).
     * O sem-args fornece uma forma prática de gerar um id novo automaticamente. Sem o canônico, cada construtor teria de repetir as
     * validações; sem o sem-args, não haveria a conveniência de criar ids gerados automaticamente.
     */

    public ShoppingCartId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ShoppingCartId(String value) {
        this(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
