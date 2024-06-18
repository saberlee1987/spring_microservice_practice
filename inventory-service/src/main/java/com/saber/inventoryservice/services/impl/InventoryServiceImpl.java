package com.saber.inventoryservice.services.impl;

import com.saber.inventoryservice.dto.BusinessException;
import com.saber.inventoryservice.dto.InventoryDto;
import com.saber.inventoryservice.dto.InventoryRequestDto;
import com.saber.inventoryservice.dto.InventoryResponse;
import com.saber.inventoryservice.mapper.InventoryMapper;
import com.saber.inventoryservice.models.command.InventoryCommand;
import com.saber.inventoryservice.models.query.Inventory;
import com.saber.inventoryservice.repositories.command.InventoryCommandRepository;
import com.saber.inventoryservice.repositories.query.InventoryQueryRepository;
import com.saber.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryQueryRepository inventoryQueryRepository;
    private final InventoryCommandRepository inventoryCommandRepository;
    private final InventoryMapper inventoryMapper;
    private final TransactionTemplate transactionTemplate;

    @Override
    public InventoryDto addInventory(InventoryRequestDto inventoryRequest) {
        if (inventoryQueryRepository.findBySkuCode(inventoryRequest.getSkuCode()).isPresent()) {
            throw new BusinessException("this skuCode before save.impossible save again");
        }
        InventoryCommand inventoryCommand = inventoryMapper.requestToCommand(inventoryRequest);
        Long inventoryId = transactionTemplate.execute(status -> inventoryCommandRepository.save(inventoryCommand));
        Inventory inventory = inventoryQueryRepository.findById(inventoryId);
        return inventoryMapper.modelToDto(inventory);
    }

    @Override
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        List<Inventory> inventories = inventoryQueryRepository.findAllBySkuCodeIn(skuCodes);
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        InventoryResponse inventoryResponse = new InventoryResponse();
        if (inventories != null && !inventories.isEmpty()) {
            for (Inventory inventory : inventories) {
                InventoryResponse inventoryResponseClone = inventoryResponse.clone();
                inventoryResponseClone.setSkuCode(inventory.getSkuCode());
                inventoryResponseClone.setIsInStock(inventory.getQuantity() != null && inventory.getQuantity() > 0);
                inventoryResponseClone.setQuantity(inventory.getQuantity());
                inventoryResponses.add(inventoryResponseClone);
            }
        }
        if (!inventoryResponses.isEmpty()){
            for (String skuCode : skuCodes) {
                if (inventoryResponses.stream().noneMatch(i->i.getSkuCode().equals(skuCode))) {
                    inventoryResponses.add(InventoryResponse.builder()
                                    .skuCode(skuCode)
                                    .quantity(0)
                                    .isInStock(false)
                            .build());
                }
            }
        }
        return inventoryResponses;
    }
}
