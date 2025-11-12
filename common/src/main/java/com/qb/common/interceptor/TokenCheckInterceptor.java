package com.qb.common.interceptor;

import com.qb.common.enums.ErrorCode;
import com.qb.common.enums.UserRole;
import com.qb.common.exception.CustomException;
import com.qb.common.security.UserContext;
import com.qb.common.security.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class TokenCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String userId = request.getHeader("X-USER-ID");
        String username = request.getHeader("X-USER-NAME");
        String role = request.getHeader("X-ROLE");

        // 인증정보가 없을 경우
        if (userId == null || role == null || username == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        UserContext context = new UserContext(
                UUID.fromString(userId),
                username,
                UserRole.valueOf(role)
        );

        // header 정보를 ThreadLocal에 저장
        UserContextHolder.set(context);
        return true; // Controller로 진행
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clear(); // ThreadLocal 메모리 누수 방지
    }
}
