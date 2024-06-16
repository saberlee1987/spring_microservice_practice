package com.saber.orderservice.repositories.command;

import com.saber.orderservice.models.command.OrderLineItemCmd;

import java.util.List;

public interface OrderLineItemCmdRepository {

    void saveAllOrderLineItemsByOrderId(List<OrderLineItemCmd> orderLineItems);

    void deleteByOrderId(Long orderId);
}
