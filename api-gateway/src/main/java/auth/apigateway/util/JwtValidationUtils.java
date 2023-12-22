package auth.apigateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;


@Component
//@RequiredArgsConstructor
public class JwtValidationUtils {

    @Value("${secret.jwt.token.secret-key}")
    String secret;

    private final Key key;

    public JwtValidationUtils(@Value("${secret.jwt.token.secret-key}") final String secretKey){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

//    private final static Key secret_key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static Environment env;

    public String getRoleByToken(String token) {
        return (String) Jwts.parserBuilder()
//                .setSigningKey(publicKeyEndpoint)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public String getEmailByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}