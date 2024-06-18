package com.saber.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class OrderLineItemRequestDto {
    @NotBlank(message = "{orderLineItems.skuCode.required}")
    private String skuCode;
    @NotNull(message = "{orderLineItems.price.required}")
    private BigDecimal price;
    @NotNull(message = "{orderLineItems.quantity.required}")
    private Integer quantity;
}
