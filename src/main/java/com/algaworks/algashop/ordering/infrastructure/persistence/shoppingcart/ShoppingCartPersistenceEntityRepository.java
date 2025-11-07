package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {

    Optional<ShoppingCartPersistenceEntity> findByCustomer_Id(UUID value);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ShoppingCartItemPersistenceEntity sci
           SET sci.price = :price, sci.totalAmount = :price * sci.quantity
         WHERE sci.productId = :productId
    """)
    void updateItemPrice(@Param("productId") UUID productId, @Param("price") BigDecimal price);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ShoppingCartItemPersistenceEntity sci
           SET sci.available = :available
         WHERE sci.productId = :productId
    """)
    void updateItemAvailability(@Param("productId") UUID productId, @Param("available") boolean available);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ShoppingCartPersistenceEntity sc
           SET sc.totalAmount = (SELECT SUM(i.totalAmount)
                                   FROM ShoppingCartItemPersistenceEntity i
                                  WHERE i.shoppingCart.id = sc.id)
         WHERE EXISTS (SELECT 1
                         FROM ShoppingCartItemPersistenceEntity sci
                        WHERE sci.shoppingCart.id = sc.id
                          AND sci.productId = :productId)
    """)
    void recalculateCartTotals(@Param("productId") UUID productId);

}
