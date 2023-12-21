package auth.apigateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtValidationUtils {

    private final static Key secret_key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String getRoleByToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public static String getEmailByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}