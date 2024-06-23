package com.saber.orderservice.controllers;

import com.saber.dto.ErrorResponseDto;
import com.saber.orderservice.dto.OrderDto;
import com.saber.orderservice.dto.OrderRequestDto;
import com.saber.orderservice.dto.OrderResponse;
import com.saber.orderservice.services.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "${service.baseUrl}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "order controller", description = "order controller")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    @Operation(method = "POST", summary = "placeOrder", description = "placeOrder"
            , operationId = "placeOrder"
            , requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = OrderRequestDto.class)
            , examples = @ExampleObject(value = """
            {
              "orderLineItems": [
                {
                  "skuCode": "iphone13",
                  "price": 1100,
                  "quantity": 3
                }
            , {
                  "skuCode": "iphone14",
                  "price": 1400,
                  "quantity": 2
                }
            , {
                  "skuCode": "A54",
                  "price": 900,
                  "quantity": 5
                },
             {
                  "skuCode": "A73",
                  "price": 1000,
                  "quantity": 6
                }
              ]
            }
            """)))
            , responses = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
    })
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethodPlaceOrder")
     @TimeLimiter(name = "inventory")
     @Retry(name = "inventory")
    public CompletableFuture<ResponseEntity<OrderDto>> placeOrder(@RequestBody OrderRequestDto orderRequest) {
        return CompletableFuture.supplyAsync(() ->ResponseEntity.ok(orderService.placeOrder(orderRequest)));
    }

    public CompletableFuture<ResponseEntity<ErrorResponseDto>> fallBackMethodPlaceOrder(OrderRequestDto orderRequest, RuntimeException runtimeException) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        errorResponseDto.setMessage("Oops! something want wrong, please order after some time!");
        errorResponseDto.setOriginalMessage(String.format("{\"code\" : %d,\"message\":\"%s\"}"
                , HttpStatus.SERVICE_UNAVAILABLE.value()
                , runtimeException.getMessage()));
        return CompletableFuture.supplyAsync(() ->ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponseDto));
    }

    @GetMapping
    @Operation(method = "GET", summary = "get all orders", description = "get all orders"
            , operationId = "get all orders"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
    })
    public ResponseEntity<OrderResponse> getAllOrders() {
        return ResponseEntity.ok(new OrderResponse(orderService.getAllOrders()));
    }

}
