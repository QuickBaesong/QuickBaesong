package com.qb.itemservice.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyHubPolicy {

	public void validateCompanyBelongsToHub(UUID companiesHub, UUID hubId){
		if (!companiesHub.equals(hubId)){
			throw new IllegalArgumentException("소속 허브에만 상품을 등록할 수 있습니다.");
		}
	}

	public void validateUserBelongsToHub(UUID usersHub, UUID hubId){
		if (!usersHub.equals(hubId)){
			throw new IllegalArgumentException("소속 허브가 관리하는 업체의 상품만 등록할 수 있습니다");
		}
	}

	public void validateUserBelongsToCompany(UUID usersCompany, UUID companyId){
		if (!usersCompany.equals(companyId)){
			throw new IllegalArgumentException("소속 업체의 상품만 등록할 수 있습니다.");
		}
	}

}
