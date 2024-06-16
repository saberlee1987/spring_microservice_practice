package com.saber.orderservice.adviser;


import com.saber.orderservice.dto.BusinessException;
import com.saber.orderservice.dto.ErrorResponseDto;
import com.saber.orderservice.dto.ValidationErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(ConstraintViolationException ex,
                                                             HttpServletRequest request) {
        log.error("Error for request url {}", request.getRequestURL());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponseDto.setCode(status.value());
        errorResponseDto.setMessage(status.getReasonPhrase());
        List<ValidationErrorDto> validations = new ArrayList<>();
        ex.getConstraintViolations().iterator().forEachRemaining(c -> {
            ValidationErrorDto validationErrorDto = new ValidationErrorDto();
            validationErrorDto.setFieldName(c.getPropertyPath().toString());
            validationErrorDto.setErrorMessage(c.getMessage());
            validations.add(validationErrorDto);
        });
        errorResponseDto.setValidations(validations);
        log.error("Error ==> {}", errorResponseDto);
        return ResponseEntity.status(status).body(errorResponseDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Error for request url {}", ((ServletWebRequest) request).getRequest().getRequestURI());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(status.value());
        errorResponseDto.setMessage(status.toString());
        List<ValidationErrorDto> validations = new ArrayList<>();
        ex.getFieldErrors().iterator().forEachRemaining(f -> {
            ValidationErrorDto validationErrorDto = new ValidationErrorDto();
            validationErrorDto.setFieldName(f.getField());
            validationErrorDto.setErrorMessage(f.getDefaultMessage());
            validations.add(validationErrorDto);
        });
        errorResponseDto.setValidations(validations);
        log.error("Error ==> {}", errorResponseDto);
        return ResponseEntity.status(status).body(errorResponseDto);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex,
                                                                    HttpServletRequest request) {
        log.error("Error for request url {}", request.getRequestURL());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        errorResponseDto.setCode(status.value());
        errorResponseDto.setMessage(status.getReasonPhrase());
        errorResponseDto.setOriginalMessage(String.format("{\"code\":\"%s\",\"message\":\"%s\"}"
                , status.value(), ex.getMessage()));
        log.error("Error ==> {}", errorResponseDto);
        return ResponseEntity.status(status).body(errorResponseDto);
    }

}
