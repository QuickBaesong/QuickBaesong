package com.qb.deliveryservice.infrastructure.client;

import com.qb.deliveryservice.infrastructure.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/internal/v1/users/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId);
}
