package com.saber.productservice.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
}