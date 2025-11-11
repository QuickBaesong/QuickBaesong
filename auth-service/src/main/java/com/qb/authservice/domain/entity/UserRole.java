package com.qb.authservice.domain.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    MASTER(true),
    HUB_MANAGER(true),
    DELIVERY_MANAGER(false), // 배송 담당자
    SUPPLIER_MANAGER(false); // 업체 담당자

    private final boolean defaultApproval;

    UserRole(boolean defaultApproval) {
        this.defaultApproval = defaultApproval;
    }
}
