package com.saber.inventoryservice.repositories.query;

import com.saber.inventoryservice.dto.InventoryDto;
import com.saber.inventoryservice.models.query.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryQueryRepository {

    List<Inventory> findAll();

    Inventory findById(Long id);

    List<Inventory> findAllBySkuCodeIn(List<String> skuCodes);

    Optional<Inventory> findBySkuCode(String skuCode);
}
