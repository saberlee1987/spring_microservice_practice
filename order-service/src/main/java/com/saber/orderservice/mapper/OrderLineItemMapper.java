package com.saber.orderservice.mapper;

import com.saber.orderservice.dto.OrderLineItemRequestDto;
import com.saber.orderservice.models.command.OrderLineItemCmd;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper
public interface OrderLineItemMapper {

    OrderLineItemCmd requestToCmd(OrderLineItemRequestDto orderLineItemRequest);
    List<OrderLineItemCmd> requestToCmd(List<OrderLineItemRequestDto> orderLineItemRequests);
}
