package com.saber.inventoryservice.repositories.command.inpl;

import com.querydsl.sql.SQLQueryFactory;
import com.saber.inventoryservice.models.command.InventoryCommand;
import com.saber.inventoryservice.models.command.QTbInventory;
import com.saber.inventoryservice.repositories.command.InventoryCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InventoryCommandRepositoryImpl implements InventoryCommandRepository {

    @Value("${spring.datasource.hikari.schema}")
    private String schema;
    private final QTbInventory tbInventory = new QTbInventory("tb_inventory", schema);
    private final SQLQueryFactory sqlQueryFactory;
    @Override
    public Long save(InventoryCommand inventory) {
        return sqlQueryFactory.insert(tbInventory)
                .populate(inventory)
                .executeWithKey(tbInventory.id);
    }
    @Override
    public void deleteById(Long id) {
        sqlQueryFactory.delete(tbInventory)
                .where(tbInventory.id.eq(id))
                .execute();
    }
}