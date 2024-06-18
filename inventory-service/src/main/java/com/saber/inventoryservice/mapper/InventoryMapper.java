package com.saber.inventoryservice.mapper;

import com.saber.inventoryservice.dto.InventoryDto;
import com.saber.inventoryservice.dto.InventoryRequestDto;
import com.saber.inventoryservice.models.command.InventoryCommand;
import com.saber.inventoryservice.models.query.Inventory;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryMapper {
    InventoryDto modelToDto(Inventory inventory);
    InventoryCommand requestToCommand(InventoryRequestDto inventoryRequest);
}