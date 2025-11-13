package com.qb.common.audit;

import com.qb.common.security.UserContext;
import com.qb.common.security.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * UserContextHolder에 저장된 현재 로그인 사용자의 정보를 가져와
 * @CreatedBy, @LastModifiedBy 필드에 자동으로 주입
 */
@Component
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        UserContext userContext = UserContextHolder.get();
        if (userContext == null) {
            log.warn("⚠️ [AuditorAware] 현재 인증된 사용자가 없습니다. createdBy 필드는 null로 설정됩니다.");
            return Optional.empty();
        }

        log.info("✅ [AuditorAware] 현재 작업자: userId={}, username={}, role={}",
                userContext.userId(), userContext.username(), userContext.role());

        return Optional.of(userContext.username());
    }
}
