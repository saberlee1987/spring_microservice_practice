package com.saber.inventoryservice.models.command;

import lombok.Data;
@Data
public class InventoryCommand {
    private Long id;
    private String skuCode;
    private Integer quantity;
}