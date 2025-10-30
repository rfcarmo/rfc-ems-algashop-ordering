package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@Table(name = "\"order\"")
public class OrderPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id; // TSID

    private UUID customerId;

    private BigDecimal totalAmount;

    private Integer totalItems;

    private String status;

    private String paymentMethod;

    private OffsetDateTime placedAt;

    private OffsetDateTime paidAt;

    private OffsetDateTime cancelledAt;

    private OffsetDateTime readyAt;
}
