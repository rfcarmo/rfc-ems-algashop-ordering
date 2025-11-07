package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
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
