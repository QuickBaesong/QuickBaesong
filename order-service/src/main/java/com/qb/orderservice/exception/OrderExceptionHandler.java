package com.qb.orderservice.exception;

import org.springframework.http.ResponseEntity;
import com.qb.common.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.qb.orderservice")
public class OrderExceptionHandler {

	@ExceptionHandler(OrderCustomException.class)
	public ResponseEntity<?> handleOrderException(OrderCustomException e){
		return ResponseEntity
			.status(e.getStatus())
			.body(new ErrorResponse(e.getErrorCode()));
	}
}
