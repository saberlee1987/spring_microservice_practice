package com.saber.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequestDto {
    @NotBlank(message = "{inventory.skuCode.required}")
    private String skuCode;
    @NotNull(message = "{inventory.quantity.required}")
    private Integer quantity;
}