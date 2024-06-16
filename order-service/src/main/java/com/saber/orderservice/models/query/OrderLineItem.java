package com.saber.orderservice.models.query;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_line_item")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderLineItem {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SKU_CODE")
    private String skuCode;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "ORDER_ID")
    private Long orderId;
}
