package com.qb.hubservice.presentation.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubSearchRequest {

    private String hubName;

    private String hubAddress;

}

