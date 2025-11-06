package com.algaworks.algashop.ordering.infrastructure.fake;

import com.algaworks.algashop.ordering.domain.model.service.OriginAddressService;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
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
