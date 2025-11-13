package com.qb.itemservice.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qb.common.enums.UserRole;
import com.qb.common.response.ApiResponse;
import com.qb.itemservice.client.CompanyServiceClient;
import com.qb.itemservice.client.HubServiceClient;
import com.qb.itemservice.client.UserServiceClient;
import com.qb.itemservice.client.dto.ResGetCompanyDto;
import com.qb.itemservice.client.dto.ResGetHubDto;
import com.qb.itemservice.client.dto.ResGetUserDto;
import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.exception.ItemCustomException;
import com.qb.itemservice.exception.ItemErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ValidateService {

	private final UserServiceClient userServiceClient;

	private final HubServiceClient hubServiceClient;

	private final CompanyServiceClient companyServiceClient;

	public void validateHubCompany(ResGetUserDto userDto, ResGetCompanyDto companyDto, ResGetHubDto hubDto, UserRole userRole) {

		if(userRole == UserRole.HUB_MANAGER){
			validateUserBelongsToHub(userDto.getCompanyId(), hubDto.getHubId());
		}

		if (userRole == UserRole.SUPPLIER_MANAGER) {
			validateUserBelongsToCompany(userDto.getCompanyId(), companyDto.getCompanyId());
		}

		validateCompanyBelongsToHub(companyDto.getHubId(), hubDto.getHubId());

	}

	public void validateItemHubCompany(Item item, UUID compay, UserRole userRole) {
		if(userRole == UserRole.HUB_MANAGER){ validateUserBelongsToHub(compay, item.getHubId()); }
	}

	/**
	 * 업체가 속한 허브가 맞는지 검증
	 * @param companiesHub
	 * @param hubId
	 */
	private void validateCompanyBelongsToHub(UUID companiesHub, UUID hubId){
		if (!companiesHub.equals(hubId)){
			throw new ItemCustomException(ItemErrorCode.INVALID_HUB);
		}
	}

	/**
	 * 허브 담당자의 근무 허브가 맞는지 검증
	 * @param usersHub
	 * @param hubId
	 */
	private void validateUserBelongsToHub(UUID usersHub, UUID hubId){
		if (!usersHub.equals(hubId)){
			throw new ItemCustomException(ItemErrorCode.INVALID_COMPANY);
		}
	}

	/**
	 * 업체 담당자가 근무하는 업체가 맞는지 검증
	 * @param usersCompany
	 * @param companyId
	 */
	private void validateUserBelongsToCompany(UUID usersCompany, UUID companyId){
		if (!usersCompany.equals(companyId)){
			throw new ItemCustomException(ItemErrorCode.INVALID_COMPANY);
		}
	}

}
