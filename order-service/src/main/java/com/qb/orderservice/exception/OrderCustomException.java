package com.qb.orderservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class OrderCustomException extends RuntimeException {

	private final OrderErrorCode errorCode;
	private final HttpStatus status;
	private final String message;

	public OrderCustomException(OrderErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
		this.status =  errorCode.getStatus();
	}
}
