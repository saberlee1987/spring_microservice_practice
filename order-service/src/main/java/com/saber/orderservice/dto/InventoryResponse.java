package com.saber.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse implements Cloneable {
    private String skuCode;
    private Boolean isInStock;
    private Integer quantity;
    @Override
    public InventoryResponse clone() {
        try {
            return (InventoryResponse) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
