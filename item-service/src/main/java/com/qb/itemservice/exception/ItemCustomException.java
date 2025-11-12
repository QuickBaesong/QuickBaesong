package com.qb.itemservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ItemCustomException extends RuntimeException {

	private ItemErrorCode errorCode;
	private String message;
	private HttpStatus status;

	public ItemCustomException(ItemErrorCode errorCode){
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
	}
}
