package com.qb.common.resolver;

import com.qb.common.annotations.CurrentUser;
import com.qb.common.enums.UserRole;
import com.qb.common.security.UserContext;
import com.qb.common.security.UserContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String HEADER_USER_ID = "X-USER-ID";
    private static final String HEADER_ROLE = "X-ROLE";
    private static final String HEADER_USERNAME = "X-USER-NAME";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        String userId = webRequest.getHeader(HEADER_USER_ID);
        String role = webRequest.getHeader(HEADER_ROLE);
        String username = webRequest.getHeader(HEADER_USERNAME);

        if (userId == null || role == null || username == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        UserContext context = new UserContext(
                UUID.fromString(userId),
                username,
                UserRole.valueOf(role)
        );

        // header 정보를 ThreadLocal에 저장
        UserContextHolder.setUser(context);

        return context;
    }
}
