package com.qb.hubservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.hubservice.application.service.HubRouteService;
import com.qb.hubservice.presentation.request.CreateHubRouteRequest;
import com.qb.hubservice.presentation.response.GetHubRouteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub-routes")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @PostMapping
    public ResponseEntity<ApiResponse<GetHubRouteResponse>> createHubRoute(
            @Valid @RequestBody CreateHubRouteRequest request
    ) {
        GetHubRouteResponse response = hubRouteService.createHubRoute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.CREATED, response));
    }

    @GetMapping("/{hubRouteId}")
    public ResponseEntity<ApiResponse<GetHubRouteResponse>> getHubRoute(
            @PathVariable UUID hubRouteId
    ) {
        GetHubRouteResponse response = hubRouteService.getHubRoute(hubRouteId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(SuccessCode.OK, response));
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<ApiResponse<ShortestRouteResponse>> searchShortestRoute(
            @Valid @ModelAttribute ShortestRouteSearchRequest request
    ) {
        // ShortestRouteService를 사용하여 최단 경로를 찾습니다.
        ShortestRouteResponse response = shortestRouteService.findShortestRoute(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(SuccessCode.OK, response));
    }


}
