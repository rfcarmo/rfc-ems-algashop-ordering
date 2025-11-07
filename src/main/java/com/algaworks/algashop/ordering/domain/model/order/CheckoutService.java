package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.product.Product;

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
