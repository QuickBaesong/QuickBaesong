package com.qb.hubservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.hubservice.presentation.request.CreateHubRouteRequest;
import com.qb.hubservice.presentation.response.GetHubRouteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hub-routes")
public class HubRouteController {

    @PostMapping
    public ResponseEntity<ApiResponse<GetHubRouteResponse>> createHubRoute(
            @Valid @RequestBody CreateHubRouteRequest request
    ) {
        GetHubRouteResponse response = hubRouteService.createHubRoute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.CREATED, response));
    }

}
