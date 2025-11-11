package com.qb.deliveryservice.infrastructure.client;

import com.qb.common.response.ApiResponse;
import com.qb.deliveryservice.infrastructure.client.dto.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service")
public interface CompanyClient {

    @GetMapping("/internal/v1/company/{companyId}")
    ApiResponse<CompanyResponse> getCompany(@PathVariable("companyId") UUID companyId);
}
