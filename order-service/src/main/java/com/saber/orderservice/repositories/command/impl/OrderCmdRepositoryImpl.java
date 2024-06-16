package com.saber.orderservice.repositories.command.impl;

import com.querydsl.sql.SQLQueryFactory;
import com.saber.orderservice.models.command.OrderCmd;
import com.saber.orderservice.models.command.QTbOrder;
import com.saber.orderservice.repositories.command.OrderCmdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderCmdRepositoryImpl implements OrderCmdRepository {

    @Value("${spring.datasource.hikari.schema}")
    private String schema;
    private final QTbOrder tbOrder = new QTbOrder("tb_order", schema);
    private final SQLQueryFactory sqlQueryFactory;

    @Override
    public Long save(OrderCmd orderCmd) {
        return sqlQueryFactory.insert(tbOrder)
                .populate(orderCmd)
                .executeWithKey(tbOrder.id);
    }
    @Override
    public void deleteById(Long id) {
        sqlQueryFactory.delete(tbOrder)
                .where(tbOrder.id.eq(id))
                .execute();
    }
}