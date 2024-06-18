package com.saber.inventoryservice.repositories.command;

import com.saber.inventoryservice.models.command.InventoryCommand;

public interface InventoryCommandRepository {

    Long save(InventoryCommand inventory);
    void  deleteById(Long id);
}
