package com.qb.common.aop;

import com.qb.common.annotations.RequiredRole;
import com.qb.common.enums.ErrorCode;
import com.qb.common.enums.UserRole;
import com.qb.common.exception.CustomException;
import com.qb.common.security.UserContext;
import com.qb.common.security.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class RoleCheckAspect {

    @Before("@annotation(com.qb.common.annotations.RequiredRole)")
    public void checkRole(JoinPoint joinPoint) {
        // 현재 사용자 정보 가져오기
        UserContext user = UserContextHolder.get();
        if (user == null) {
            log.warn("🚨 인증되지 않은 사용자 요청입니다.");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 메서드에서 RequiredRole 어노테이션 추출
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiredRole requiredRole = method.getAnnotation(RequiredRole.class);

        // 허용된 역할 목록
        UserRole[] allowedRoles = requiredRole.value();

        // 사용자 권한이 목록에 있는지 확인
        boolean authorized = Arrays.stream(allowedRoles)
                .anyMatch(role -> role == user.role());

        if (!authorized) {
            log.warn("🚫 권한 부족: userId={}, role={}, allowed={}",
                    user.userId(), user.role(), Arrays.toString(allowedRoles));
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        log.debug("✅ 권한 검증 통과: userId={}, role={}", user.userId(), user.role());
    }
}
