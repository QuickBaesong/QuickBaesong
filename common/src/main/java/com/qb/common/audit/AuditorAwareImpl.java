package com.qb.common.audit;

import com.qb.common.security.UserContextHolder;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * UserContextHolder에 저장된 현재 로그인 사용자의 정보를 가져와
 * @CreatedBy, @LastModifiedBy 필드에 자동으로 주입
 */
@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        var user = UserContextHolder.get();
        if (user != null && user.userId() != null) {
            return Optional.of(user.userId());
        }
        return Optional.empty();
    }
}
