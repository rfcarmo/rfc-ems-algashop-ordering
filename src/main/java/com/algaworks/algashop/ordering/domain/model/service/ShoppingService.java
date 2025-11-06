package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.utility.DomainService;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final Customers customerDomainRepository;
    private final ShoppingCarts shoppingCartDomainRepository;

    public ShoppingCart startShopping(CustomerId customerId) {
        if(!customerDomainRepository.exists(customerId)) {
            throw new CustomerNotFoundException();
        }

        shoppingCartDomainRepository.ofCustomer(customerId)
                .ifPresent(cart -> {
                    throw new CustomerAlreadyHaveShoppingCartException();
                });

        return ShoppingCart.startShopping(customerId);
    }

}
