package com.qb.gateway.filter;

import com.qb.gateway.component.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
/** Gateway 단에서 모든 요청에 대한 AccessToken 검증 및 헤더에 User 정보를 실어 전달 */
public class JwtAuthenticationFilter implements GatewayFilter {
    private final JwtTokenValidator validator;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange);

        if (token != null && validator.validateToken(token)) {
            Claims claims = validator.parse(token);

            exchange.getRequest()
                    .mutate()
                    .header("X-USER-ID", claims.getSubject())
                    .header("X-ROLE", claims.get("role", String.class))
                    .header("X-USER-NAME", claims.get("username",String.class))
                    .build();
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ?
                authHeader.substring(7) : null;
    }


}
