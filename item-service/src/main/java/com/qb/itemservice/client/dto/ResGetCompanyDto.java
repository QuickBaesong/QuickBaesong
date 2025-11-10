package com.qb.itemservice.client.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResGetCompanyDto {
	@JsonProperty("company_id")
	private UUID companyId;
	@JsonProperty("company_name")
	private String companyName;
	@JsonProperty("hub_id")
	private UUID hubId;
	@JsonProperty("company_type")
	private String companyType;
}
