package com.saber.orderservice.models.command;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderLineItemCmd {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    private Long orderId;
}
