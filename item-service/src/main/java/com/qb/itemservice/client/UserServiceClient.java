package com.qb.itemservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.qb.common.response.ApiResponse;
import com.qb.itemservice.client.dto.ResGetUserDto;

@FeignClient(name = "auth-service")
public interface UserServiceClient {

	@GetMapping("/v1/user/{userId}")
	ApiResponse<ResGetUserDto> getUser(@PathVariable UUID userId);

}
