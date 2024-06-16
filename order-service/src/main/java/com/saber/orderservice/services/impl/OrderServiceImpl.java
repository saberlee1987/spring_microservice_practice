package com.saber.orderservice.services.impl;

import com.saber.orderservice.dto.OrderDto;
import com.saber.orderservice.dto.OrderRequestDto;
import com.saber.orderservice.mapper.OrderLineItemMapper;
import com.saber.orderservice.mapper.OrderMapper;
import com.saber.orderservice.models.command.OrderCmd;
import com.saber.orderservice.models.command.OrderLineItemCmd;
import com.saber.orderservice.models.query.Order;
import com.saber.orderservice.repositories.command.OrderLineItemCmdRepository;
import com.saber.orderservice.repositories.command.OrderCmdRepository;
import com.saber.orderservice.repositories.query.OrderQueryRepository;
import com.saber.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderCmdRepository orderCmdRepository;
    private final OrderLineItemCmdRepository orderLineItemCmdRepository;
    private final OrderQueryRepository orderQueryRepository;

    private final TransactionTemplate transactionTemplate;

    private final OrderLineItemMapper orderLineItemMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(OrderRequestDto orderRequest) {

        OrderCmd orderCmd = new OrderCmd();
        orderCmd.setOrderNumber(createOrderNumber());
        Long newOrderId = transactionTemplate.execute(status -> {
            Long orderId = orderCmdRepository.save(orderCmd);
            List<OrderLineItemCmd> orderLineItems = orderLineItemMapper.requestToCmd(orderRequest.getOrderLineItems());
            for (OrderLineItemCmd orderLineItem : orderLineItems) {
                orderLineItem.setOrderId(orderId);
            }
            orderLineItemCmdRepository.saveAllOrderLineItemsByOrderId(orderLineItems);
            return orderId;
        });
        Order order = orderQueryRepository.findById(newOrderId);
        return orderMapper.modelToDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderQueryRepository.findAllOrders();
        return orderMapper.modelToDto(orders);
    }

    private Long createOrderNumber() {
        Long orderNumber = orderQueryRepository.getLastOrderNumber();
        if (orderNumber == null) orderNumber = 1_000L;
        else orderNumber += 1;
        return orderNumber;
    }
}
