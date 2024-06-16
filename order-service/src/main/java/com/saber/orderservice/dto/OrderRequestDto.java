package com.saber.orderservice.dto;

import lombok.Data;
import java.util.List;
@Data
public class OrderRequestDto {
    private List<OrderLineItemRequestDto> orderLineItems;
}