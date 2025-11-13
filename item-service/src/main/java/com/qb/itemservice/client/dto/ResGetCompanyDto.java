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
	private UUID companyId;
	private String companyName;
	private UUID hubId;
	private String companyType;
}
