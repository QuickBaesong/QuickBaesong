package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.entity.CompanyType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanySearchRequest {

  private String name;
  private CompanyType type;
  private UUID hubId;
  private String sortBy = "companyName";
  private int size = 20;
  private int page = 0;
}