package com.qb.itemservice.client.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResGetHubDto {
	@JsonProperty("hub_id")
	private UUID hubId;
	@JsonProperty("hub_name")
	private String hubName;
}
