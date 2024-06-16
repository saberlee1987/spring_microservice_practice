package com.saber.orderservice.mapper;

import com.saber.orderservice.dto.OrderDto;
import com.saber.orderservice.models.query.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderDto modelToDto(Order order);
    List<OrderDto> modelToDto(List<Order> orders);
}
