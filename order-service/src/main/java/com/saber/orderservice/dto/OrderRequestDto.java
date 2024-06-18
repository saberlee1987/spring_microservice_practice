package com.saber.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
@Data
public class OrderRequestDto {
    @NotNull(message = "{order.orderLineItems.required}")
    private List<@NotEmpty(message = "{order.orderLineItems.required}") @Valid OrderLineItemRequestDto> orderLineItems;
}