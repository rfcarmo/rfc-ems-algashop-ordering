package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.model.utility.DomainService;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;

@DomainService
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart, Billing billing, Shipping shipping, PaymentMethod paymentMethod) {
        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException(shoppingCart.id(), "Shopping cart contains unavailable items or is empty");
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBillingInfo(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        shoppingCart.items()
            .forEach(i -> order.addItem(new Product(i.productId(), i.productName(), i.price(), i.isAvailable()), i.quantity()));

        order.place();
        shoppingCart.empty();

        return order;
    }
}
