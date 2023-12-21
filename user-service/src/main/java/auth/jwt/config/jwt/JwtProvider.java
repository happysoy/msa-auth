package auth.jwt.config.jwt;

import auth.jwt.domain.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final long ACCESS_TOKEN_TIME = 30 * 60 *1000; // 30분
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000; // 7일


    private final Key secret_key = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    /**
     * Access Token 생성
     * @ return
     */
    public String createAccessToken(String email, Role role) {
        Claims claims = Jwts.claims();
        claims.setSubject(email); // 회원 정보 찾을 때 사용
        claims.put("role", role); // 접근 권한 관리
        Date now = new Date();

        return Jwts.builder()
//                .setHeaderParam("algorithm-type", "jwt")
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(getExpireDateAccessToken(ACCESS_TOKEN_TIME)) // 토큰 만료 시간
                .signWith(secret_key)
                .compact();

    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims();
        claims.setSubject(email); // 토큰의 용도 명시
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(getExpireDateAccessToken(REFRESH_TOKEN_TIME))
                .signWith(secret_key)
                .compact();

    }

    public String getEmailByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public Date getExpireDateAccessToken(long expireTimeMils) {
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }

}
