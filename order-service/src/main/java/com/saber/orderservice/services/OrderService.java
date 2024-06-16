package com.saber.orderservice.services;

import com.saber.orderservice.dto.OrderDto;
import com.saber.orderservice.dto.OrderRequestDto;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder(OrderRequestDto orderRequest);
    List<OrderDto> getAllOrders();
}
