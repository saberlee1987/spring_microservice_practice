package com.saber.orderservice.services.impl;

import com.saber.orderservice.dto.*;
import com.saber.orderservice.mapper.OrderLineItemMapper;
import com.saber.orderservice.mapper.OrderMapper;
import com.saber.orderservice.models.command.OrderCmd;
import com.saber.orderservice.models.command.OrderLineItemCmd;
import com.saber.orderservice.models.query.Order;
import com.saber.orderservice.repositories.command.OrderCmdRepository;
import com.saber.orderservice.repositories.command.OrderLineItemCmdRepository;
import com.saber.orderservice.repositories.query.OrderQueryRepository;
import com.saber.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

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

    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public OrderDto placeOrder(OrderRequestDto orderRequest) {

        checkExistProductInStock(orderRequest.getOrderLineItems());
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

    private void checkExistProductInStock(List<OrderLineItemRequestDto> orderLineItems) {
        List<String> skuCodes = orderLineItems.stream().map(OrderLineItemRequestDto::getSkuCode).toList();
        String url = "http://localhost:9602/api/inventory";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        url = uriBuilder.queryParam("skuCodes", skuCodes).toUriString();
        ResponseEntity<InventoryResponseDto> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, InventoryResponseDto.class);

        if (responseEntity.getBody() == null) {
            throw new BusinessException("Inventory service is return null.please contact admin system");
        }
        List<InventoryResponse> inventoryResponses = responseEntity.getBody().getResponses();
        if (inventoryResponses.isEmpty()
                || !inventoryResponses.stream().allMatch(InventoryResponse::getIsInStock)) {
            List<String> skuCodesNotInStock = responseEntity.getBody().getResponses()
                    .stream().filter(r -> !r.getIsInStock())
                    .map(InventoryResponse::getSkuCode)
                    .toList();
            throw new BusinessException(String.format("Product %s is not in stock , please try again later",
                    skuCodesNotInStock
            ));
        }
        for (InventoryResponse inventory : inventoryResponses) {
            boolean orderQuantityExistInStock = orderLineItems.stream()
                    .filter(o -> o.getSkuCode().equals(inventory.getSkuCode()))
                    .anyMatch(o -> o.getQuantity().compareTo(inventory.getQuantity()) <=0);
            if (!orderQuantityExistInStock) {
                OrderLineItemRequestDto orderLineItemRequestDto = orderLineItems.stream()
                        .filter(o -> o.getSkuCode().equals(inventory.getSkuCode())).findAny().orElse(null);
                if (orderLineItemRequestDto != null)
                    throw new BusinessException(String.format("Product %s for quantity %d not exist in stock",
                            orderLineItemRequestDto.getSkuCode()
                            , orderLineItemRequestDto.getQuantity()
                    ));
            }
        }
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
