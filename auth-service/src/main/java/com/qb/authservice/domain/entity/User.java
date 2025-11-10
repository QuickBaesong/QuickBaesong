package com.qb.authservice.domain.entity;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "p_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username; // 로그인 ID

    @Column(name = "user_k_name", nullable = false)
    private String userKname; // 이름

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "slack_id", nullable = false)
    private String slackId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_approved")
    private boolean isApproved;

    public void approveUser() {
        this.isApproved = true;
    }

}
