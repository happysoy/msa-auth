package auth.apigateway.filter;
import auth.apigateway.util.JwtValidationUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.security.SignatureException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
//@RequiredArgsConstructor
public class RoleAdminFilter extends AbstractGatewayFilterFactory<RoleAdminFilter.NameConfig> {

    public RoleAdminFilter(JwtValidationUtils jwtUtils) {
        super(NameConfig.class);
        this.jwtUtils = jwtUtils;
    }

    private final JwtValidationUtils jwtUtils;

    String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Override
    public GatewayFilter apply(NameConfig config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            List<String> authHeader = request.getHeaders().get(ACCESS_TOKEN);

            if (Objects.isNull(authHeader) || Objects.equals(authHeader.get(0), "null")) {
                return onError(exchange, "액세스 토큰이 없습니다");
            }


            String token = authHeader.get(0);

            if (!jwtUtils.getRoleByToken(token).equals("ADMIN"))
                return onError(exchange, "관리자 전용 페이지입니다");

            try {
                String email = jwtUtils.getEmailByToken(token);
                log.info("관리자 인증 완료={}", email);
            } catch (ExpiredJwtException e) {
                return onError(exchange, "토큰이 만료되었습니다");
            }
            catch (IllegalArgumentException e) {
                return onError(exchange, "적합하지 않은 파라미터입니다");
            }
            catch (MalformedJwtException e) {
                return onError(exchange, "손상된 토큰입니다");
            } catch (UnsupportedJwtException e) {
                return onError(exchange, "지원하지 않는 토큰입니다");
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{

            }));
        });
    }


    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        log.error(message);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }


}