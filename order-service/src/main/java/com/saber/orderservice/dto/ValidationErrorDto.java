package com.saber.orderservice.dto;

import lombok.Data;

@Data
public class ValidationErrorDto {
    private String fieldName;
    private String errorMessage;
}
