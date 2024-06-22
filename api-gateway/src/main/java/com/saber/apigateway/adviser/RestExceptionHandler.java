package com.saber.apigateway.adviser;


import com.saber.dto.ErrorResponseDto;
import com.saber.dto.ServiceErrorResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
	
	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<Object> handleAuthentication(AuthenticationException exception, ServerWebExchange exchange) {
		ErrorResponseDto errorResponse = new ErrorResponseDto();
		errorResponse.setCode(ServiceErrorResponseEnum.AUTHENTICATION_EXCEPTION.getCode());
		errorResponse.setMessage(ServiceErrorResponseEnum.AUTHENTICATION_EXCEPTION.getMessage());
		errorResponse.setOriginalMessage(String.format("{\"code\":%s,\"text\":\"%s\"}",ServiceErrorResponseEnum.AUTHENTICATION_EXCEPTION.getCode(),exception.getMessage() ));
		log.error("Error for handleAuthentication with body ===> {}",errorResponse);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(value = AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AuthenticationCredentialsNotFoundException exception) {
		ErrorResponseDto errorResponse = new ErrorResponseDto();
		errorResponse.setCode(ServiceErrorResponseEnum.AUTHENTICATION_EXCEPTION.getCode());
		errorResponse.setMessage(ServiceErrorResponseEnum.AUTHENTICATION_EXCEPTION.getMessage());
		errorResponse.setOriginalMessage(String.format("{\"code\":%s,\"text\":\"%s\"}",ServiceErrorResponseEnum.AUTHENTICATION_EXCEPTION.getCode(),exception.getMessage() ));
		log.error("Error for handleAuthentication with body ===> {}",errorResponse);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

}
