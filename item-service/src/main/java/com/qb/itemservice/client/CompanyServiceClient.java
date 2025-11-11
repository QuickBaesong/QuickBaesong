package com.qb.itemservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.qb.common.response.ApiResponse;
import com.qb.itemservice.client.dto.ResGetCompanyDto;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {

	@GetMapping("/v1/companies/{companyId}")
	ApiResponse<ResGetCompanyDto> getCompany(@PathVariable("companyId") UUID companyId);

}
