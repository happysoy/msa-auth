//package auth.apigateway.filter;
//
//import auth.apigateway.util.JwtValidationUtils;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.security.Key;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {
//
//    private final JwtValidationUtils jwtUtils;
//    private final static Key secret_key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//
//    String ACCESS_TOKEN = "ACCESS_TOKEN";
//
//    @Override
//    public GatewayFilter apply(NameConfig config) {
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            log.info("Request header ={}", request.getHeaders());
//
//            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                return onError(exchange, "토큰에 헤더가 없습니다");
//            }
//            List<String> authHeader = request.getHeaders().get(ACCESS_TOKEN);
//
//            if (Objects.isNull(authHeader)) {
//                return onError(exchange, "access token 이 없습니다");
//            }
//
//            String token = authHeader.get(0);
//
//            try {
//                String email = JwtValidationUtils.getEmailByToken(token);
//                log.info("인증 완료={}", email);
//            } catch (ExpiredJwtException e) {
//                return onError(exchange, "토큰이 만료되었습니다");
//            } catch (IllegalArgumentException | MalformedJwtException exception) {
//                return onError(exchange, "권한이 없습니다");
//            }
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//
//            }));
//        });
//    }
//
//
//    private Mono<Void> onError(ServerWebExchange exchange, String message) {
//        log.error(message);
//
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//
//        return response.setComplete();
//    }
//
//
//}