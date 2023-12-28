package auth.jwt.config.jwt;

import auth.jwt.domain.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
//@RequiredArgsConstructor
public class JwtProvider {

    private static final long ACCESS_TOKEN_TIME = 30 * 60 *1000; // 30분
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000; // 7일


//    private final Key secret_key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${secret.jwt.token.secret-key}")
    String secret;

    private final Key key;

    public JwtProvider(@Value("${secret.jwt.token.secret-key}") final String secretKey){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }




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
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(getExpireDateAccessToken(ACCESS_TOKEN_TIME)) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)
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
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

    }

    public String getEmailByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public Date getExpireDateAccessToken(long expireTimeMils) {
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }



    // 만료시간 추출
    public Date getExpirationDate(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }


}
