package com.qb.itemservice.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.qb.itemservice.exception.ItemCustomException;
import com.qb.itemservice.exception.ItemErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyHubPolicy {

	/**
	 * 업체가 속한 허브가 맞는지 검증
	 * @param companiesHub
	 * @param hubId
	 */
	public void validateCompanyBelongsToHub(UUID companiesHub, UUID hubId){
		if (!companiesHub.equals(hubId)){
			throw new ItemCustomException(ItemErrorCode.INVALID_HUB);
		}
	}

	/**
	 * 허브 담당자의 근무 허브가 맞는지 검증
	 * @param usersHub
	 * @param hubId
	 */
	public void validateUserBelongsToHub(UUID usersHub, UUID hubId){
		if (!usersHub.equals(hubId)){
			throw new ItemCustomException(ItemErrorCode.INVALID_COMPANY);
		}
	}

	/**
	 * 업체 담당자가 근무하는 업체가 맞는지 검증
	 * @param usersCompany
	 * @param companyId
	 */
	public void validateUserBelongsToCompany(UUID usersCompany, UUID companyId){
		if (!usersCompany.equals(companyId)){
			throw new ItemCustomException(ItemErrorCode.INVALID_COMPANY);
		}
	}

}
