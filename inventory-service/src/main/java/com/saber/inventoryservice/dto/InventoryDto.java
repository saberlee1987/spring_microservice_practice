package com.saber.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryDto {
    private Long id;
    private String skuCode;
    private Integer quantity;
}