package com.saber.orderservice.dto;

import lombok.Data;
import java.util.List;
@Data
public class OrderDto {
    private Long id;
    private Long orderNumber;
    private List<OrderLineItemDto> orderLineItems;
}