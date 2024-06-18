package com.saber.inventoryservice.services;

import com.saber.inventoryservice.dto.InventoryDto;
import com.saber.inventoryservice.dto.InventoryRequestDto;
import com.saber.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryDto addInventory(InventoryRequestDto inventoryRequest);

    List<InventoryResponse> isInStock(List<String> skuCodes);

}
