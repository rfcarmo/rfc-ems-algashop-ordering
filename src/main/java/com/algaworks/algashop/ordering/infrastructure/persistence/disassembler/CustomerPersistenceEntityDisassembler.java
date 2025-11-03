package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomainEntity(CustomerPersistenceEntity customerPersistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(customerPersistenceEntity.getId()))
                .fullName(new FullName(customerPersistenceEntity.getFirstName(), customerPersistenceEntity.getLastName()))
                .birthDate(new BirthDate(customerPersistenceEntity.getBirthDate()))
                .email(new Email(customerPersistenceEntity.getEmail()))
                .phone(new Phone(customerPersistenceEntity.getPhone()))
                .document(new Document(customerPersistenceEntity.getDocument()))
                .promotionNotificationsAllowed(customerPersistenceEntity.getPromotionNotificationsAllowed())
                .archived(customerPersistenceEntity.getArchived())
                .registeredAt(customerPersistenceEntity.getRegisteredAt())
                .archivedAt(customerPersistenceEntity.getArchivedAt())
                .loyaltyPoints(new LoyaltyPoints(customerPersistenceEntity.getLoyaltyPoints()))
                .address(this.address(customerPersistenceEntity.getAddress()))
                .version(customerPersistenceEntity.getVersion())
                .build();
    }

    private Address address(AddressEmbeddable addressEmbeddable) {
        Objects.requireNonNull(addressEmbeddable);

        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .complement(addressEmbeddable.getComplement())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

}
