package com.saber.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderLineItemRequestDto {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
