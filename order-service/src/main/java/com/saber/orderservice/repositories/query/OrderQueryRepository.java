package com.saber.orderservice.repositories.query;

import com.saber.orderservice.models.query.Order;

import java.util.List;

public interface OrderQueryRepository {

    Long getLastOrderNumber();

    List<Order> findAllOrders();

    Order findById(Long orderId);
}
