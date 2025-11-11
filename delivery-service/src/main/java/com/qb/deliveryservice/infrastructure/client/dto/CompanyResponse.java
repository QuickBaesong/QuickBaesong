package com.qb.deliveryservice.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyResponse {
    private UUID companyId;
    private String companyName;  // 업체명만 필요
}
