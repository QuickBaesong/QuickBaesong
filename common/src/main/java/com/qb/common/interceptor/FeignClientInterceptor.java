package com.qb.common.interceptor;

import com.qb.common.security.UserContext;
import com.qb.common.security.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
/** FeignClient는 Spring이 내부적으로 HTTP 요청을 생성하므로, interceptor를 통해 헤더를 삽입해야 함. */
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        UserContext context = UserContextHolder.get();

        if (context != null) {
            template.header("X-USER-ID", context.userId().toString());
            template.header("X-ROLE", context.role().name());
            template.header("X-USER-NAME", context.username());
        }
    }
}
