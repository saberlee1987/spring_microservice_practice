package com.saber.inventoryservice.repositories.query.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.saber.inventoryservice.models.query.Inventory;
import com.saber.inventoryservice.repositories.query.InventoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.saber.inventoryservice.models.query.QInventory.inventory;

@Repository
@RequiredArgsConstructor
public class InventoryQueryRepositoryImpl implements InventoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Inventory> findAll() {
        return jpaQueryFactory.selectFrom(inventory)
                .fetch();
    }

    @Override
    public Inventory findById(Long id) {
        return jpaQueryFactory.selectFrom(inventory)
                .where(inventory.id.eq(id))
                .fetchOne();
    }

    @Override
   public List<Inventory> findAllBySkuCodeIn(List<String> skuCodes) {
        return jpaQueryFactory.selectFrom(inventory)
                .where(inventory.skuCode.in(skuCodes))
                .fetch();
    }

    @Override
    public Optional<Inventory> findBySkuCode(String skuCode) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(inventory)
                .where(inventory.skuCode.eq(skuCode))
                .fetchOne());
    }
}
