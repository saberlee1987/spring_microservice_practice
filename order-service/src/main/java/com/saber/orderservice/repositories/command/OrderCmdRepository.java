package com.saber.orderservice.repositories.command;

import com.saber.orderservice.models.command.OrderCmd;

public interface OrderCmdRepository {
    Long save(OrderCmd orderCmd);
    void  deleteById(Long id);
}
