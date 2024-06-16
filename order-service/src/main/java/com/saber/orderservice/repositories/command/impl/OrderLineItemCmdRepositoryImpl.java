package com.saber.orderservice.repositories.command.impl;

import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import com.saber.orderservice.models.command.OrderLineItemCmd;
import com.saber.orderservice.models.command.QTbOrderLineItem;
import com.saber.orderservice.repositories.command.OrderLineItemCmdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderLineItemCmdRepositoryImpl implements OrderLineItemCmdRepository {


    @Value("${spring.datasource.hikari.schema}")
    private String schema;
    private final QTbOrderLineItem tbOrderLineItem = new QTbOrderLineItem("tb_order_line_item", schema);
    private final SQLQueryFactory sqlQueryFactory;

    @Override
    public void saveAllOrderLineItemsByOrderId(List<OrderLineItemCmd> orderLineItems) {
        if (!orderLineItems.isEmpty()) {
            SQLInsertClause insertClause = sqlQueryFactory.insert(tbOrderLineItem);
            for (OrderLineItemCmd orderLineItem : orderLineItems) {
                insertClause.populate(orderLineItem)
                        .addBatch();
            }
            insertClause.execute();
        }
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        sqlQueryFactory.delete(tbOrderLineItem)
                .where(tbOrderLineItem.orderId.eq(orderId))
                .execute();
    }
}
