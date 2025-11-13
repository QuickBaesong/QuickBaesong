package com.qb.itemservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.qb.common.interceptor.FeignClientInterceptor;
import com.qb.common.response.ApiResponse;
import com.qb.itemservice.client.dto.ResGetUserDto;

@FeignClient(name = "auth-service", configuration = FeignClientInterceptor.class)
public interface UserServiceClient {

	@GetMapping("/v1/users/{userName}")
	ApiResponse<ResGetUserDto> getUser(@PathVariable String userName);

}
