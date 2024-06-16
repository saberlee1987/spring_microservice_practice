package com.saber.orderservice.models.command;

import lombok.Data;
@Data
public class OrderCmd {
    private Long id;
    private Long orderNumber;
}