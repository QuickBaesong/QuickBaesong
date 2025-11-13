package com.qb.common.resolver;

import com.qb.common.annotations.CurrentUser;
import com.qb.common.enums.ErrorCode;
import com.qb.common.exception.CustomException;
import com.qb.common.security.UserContext;
import com.qb.common.security.UserContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

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

        UserContext userContext = UserContextHolder.get();
        if (userContext == null) {
            throw new CustomException(ErrorCode.USER_CONTEXT_NOT_SET);
        }
        return userContext;
    }
}
