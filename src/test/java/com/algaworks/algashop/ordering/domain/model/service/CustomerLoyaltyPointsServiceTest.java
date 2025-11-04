package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {

    CustomerLoyaltyPointsService loyaltyPointsService = new CustomerLoyaltyPointsService();

    @Test
    public void givenAValidCustomerAndOrder_whenAddingPoints_thenShouldAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();

        loyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(55));
    }

    @Test
    public void givenAValidCustomerAndOrderWithLowAmount_whenAddingPoints_thenShouldNotAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Product product = ProductTestDataBuilder.aAltProductLowPrice().build();

        Order order = OrderTestDataBuilder.anOrder()
                .withItems(false)
                .status(OrderStatus.DRAFT)
                .shipping(OrderTestDataBuilder.aAltFreeShipping())
                .build();

        order.addItem(product, new Quantity(10));

        order.place();
        order.markAsPaid();
        order.markAsReady();

        loyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(20));
    }

}