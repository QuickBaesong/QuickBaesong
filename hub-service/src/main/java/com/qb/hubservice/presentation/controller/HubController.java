package com.qb.hubservice.presentation.controller;

import com.qb.common.response.ApiResponse;
import com.qb.common.enums.SuccessCode;
import com.qb.common.response.PageResponse;
import com.qb.hubservice.application.service.HubService;
import com.qb.hubservice.presentation.request.CreateHubRequest;
import com.qb.hubservice.presentation.request.HubSearchRequest;
import com.qb.hubservice.presentation.response.GetHubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub")
public class HubController {

    private final HubService hubService;
    private static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 30, 50);

    @PostMapping
    public ResponseEntity<ApiResponse<GetHubResponse>> createHub(@RequestBody @Valid CreateHubRequest request) {
        GetHubResponse response = hubService.createHub(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.CREATED, response));
    }


    @GetMapping("/{hubId}")
    public ResponseEntity<ApiResponse<GetHubResponse>> getHub(@PathVariable UUID hubId) {
        GetHubResponse response = hubService.getHub(hubId);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.OK, response));
    }

    @PostMapping("/dummy")
    public ResponseEntity<ApiResponse<List<GetHubResponse>>> createDummyHubs() {

        List<GetHubResponse> responses = hubService.createAllDummyHubs();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.CREATED, responses));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GetHubResponse>>> getPageHub(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction =DESC
            ) Pageable pageable, HubSearchRequest searchRequest) {

        int requestedSize = pageable.getPageSize();

        if (!ALLOWED_PAGE_SIZES.contains(requestedSize)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("허용되지 않은 페이지 크기 요청입니다. (요청된 크기: %d). 허용되는 크기는 %s 입니다.",
                            requestedSize, ALLOWED_PAGE_SIZES));
        }


        Page<GetHubResponse> responsePage = hubService.getPageHubs(pageable,searchRequest);

        PageResponse<GetHubResponse> pageResponse = PageResponse.from(responsePage);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.OK, pageResponse));
    }




}