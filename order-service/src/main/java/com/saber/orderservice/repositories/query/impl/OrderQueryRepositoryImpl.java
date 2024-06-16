package com.saber.orderservice.repositories.query.impl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.saber.orderservice.models.query.Order;
import com.saber.orderservice.models.query.QOrder;
import com.saber.orderservice.repositories.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.saber.orderservice.models.query.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public static final QOrder order2 = new QOrder("order2");

    @Override
    public Long getLastOrderNumber() {
        return jpaQueryFactory.select(order.orderNumber).from(order)
                .where(order.id.eq(
                        JPAExpressions.select(order2.id.max()).from(order2)
                ))
                .fetchOne();
    }

    @Override
    public List<Order> findAllOrders() {
        return jpaQueryFactory.selectFrom(order)
                .fetch();
    }

    @Override
    public Order findById(Long orderId) {
        return jpaQueryFactory.selectFrom(order)
                .where(order.id.eq(orderId))
                .fetchOne();
    }
}
