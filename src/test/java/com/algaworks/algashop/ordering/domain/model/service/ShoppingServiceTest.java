package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

    @InjectMocks
    private ShoppingService shoppingService;

    @Mock
    private ShoppingCarts shoppingCarts;

    @Mock
    private Customers customers;

    @Test
    void givenExistingCustomerAndNoShoppingCart_whenStartShopping_shouldReturnNewShoppingCart() {
        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Mockito.when(customers.exists(customerId)).thenReturn(true);
        Mockito.when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.empty());

        ShoppingCart newShoppingCart = shoppingService.startShopping(customerId);

        Assertions.assertThat(newShoppingCart).isNotNull();
        Assertions.assertThat(newShoppingCart.customerId()).isEqualTo(customerId);
        Assertions.assertThat(newShoppingCart.isEmpty()).isTrue();
        Assertions.assertThat(newShoppingCart.totalAmount()).isEqualTo(com.algaworks.algashop.ordering.domain.model.valueobject.Money.ZERO);
        Assertions.assertThat(newShoppingCart.totalItems()).isEqualTo(com.algaworks.algashop.ordering.domain.model.valueobject.Quantity.ZERO);

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCarts).ofCustomer(customerId);
    }

    @Test
    void givenNonExistingCustomer_whenStartShopping_shouldThrowCustomerNotFoundException() {
        CustomerId customerId = new CustomerId();

        Mockito.when(customers.exists(customerId)).thenReturn(false);

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCarts, Mockito.never()).ofCustomer(Mockito.any());
    }

    @Test
    void givenExistingCustomerAndExistingShoppingCart_whenStartShopping_shouldThrowCustomerAlreadyHaveShoppingCartException() {
        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
        ShoppingCart existingCart = ShoppingCartTestDataBuilder.aShoppingCart().customerId(customerId).build();

        Mockito.when(customers.exists(customerId)).thenReturn(true);
        Mockito.when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.of(existingCart));

        Assertions.assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCarts).ofCustomer(customerId);
    }

}