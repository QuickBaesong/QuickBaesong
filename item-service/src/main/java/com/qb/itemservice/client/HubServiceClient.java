package com.qb.itemservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.qb.itemservice.client.dto.ResGetHubDto;

@FeignClient(name = "hub-service")
public interface HubServiceClient {

	@GetMapping(value = "/v1/hub/{hubId}")
	ResGetHubDto getHub(@PathVariable("hubId") UUID hubId);

}
