package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

public class ProductTestDataBuilder {

    private ProductTestDataBuilder() { }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .productId(new ProductId())
                .productName(new ProductName("Sample Product"))
                .price(new Money("10.00"))
                .inStock(true);
    }

    public static Product.ProductBuilder aUnavailableProduct() {
        return Product.builder()
                .productId(new ProductId())
                .productName(new ProductName("Unavailable Product"))
                .price(new Money("20.00"))
                .inStock(false);
    }

    public static Product.ProductBuilder aAltProductMemoryRam() {
        return Product.builder()
                .productId(new ProductId())
                .productName(new ProductName("Memory RAM 16GB"))
                .price(new Money("50.00"))
                .inStock(true);
    }

    public static Product.ProductBuilder aAltProductSSD() {
        return Product.builder()
                .productId(new ProductId())
                .productName(new ProductName("SSD 512GB"))
                .price(new Money("70.00"))
                .inStock(true);
    }

}
