package com.qb.gateway.filter;

import com.qb.gateway.component.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
@Slf4j
/** Gateway 단에서 모든 요청에 대한 AccessToken 검증 및 헤더에 User 정보를 실어 전달 */
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtTokenValidator validator;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange);

        if (token != null && validator.validateToken(token)) {
            Claims claims = validator.parse(token);
            log.info("✅ Gateway: token validated. userId={}, role={}, username={}",
                    claims.getSubject(),
                    claims.get("role", String.class),
                    claims.get("username", String.class));

            // 기존 request를 기반으로 새 request를 생성
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-USER-ID", claims.getSubject())
                    .header("X-ROLE", claims.get("role", String.class))
                    .header("X-USER-NAME", claims.get("username", String.class))
                    .build();

            // 새로운 request를 포함한 exchange로 교체
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(request)
                    .build();

            return chain.filter(mutatedExchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // SecurityFilter보다 먼저 실행
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ?
                authHeader.substring(7) : null;
    }


}
