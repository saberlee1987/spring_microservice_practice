package com.saber.dto;

import lombok.Data;

@Data
public class ValidationErrorDto {
    private String fieldName;
    private String errorMessage;
}
