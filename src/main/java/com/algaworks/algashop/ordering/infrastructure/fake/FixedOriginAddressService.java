package com.algaworks.algashop.ordering.infrastructure.fake;

import com.algaworks.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {

    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Av. Europa")
                .number("1777")
                .complement("Galpão 7")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode(new ZipCode("01000000"))
                .build();
    }
}
