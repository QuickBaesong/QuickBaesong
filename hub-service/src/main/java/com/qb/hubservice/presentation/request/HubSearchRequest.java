package com.qb.hubservice.presentation.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubSearchRequest {

    @NotBlank(message = "허브 이름은 필수입니다.")
    @Size(max = 255, message = "허브 이름은 255자 이하이어야 합니다.")
    private String hubName;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 255, message = "주소는 255자 이하이어야 합니다.")
    private String hubAddress;

}

