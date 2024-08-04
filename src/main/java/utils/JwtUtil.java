package utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    @Getter
    private static final SecretKey key = initSecretKey();

    protected static SecretKey initSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    public static String generateToken(Map<String, ?> claims, Date expiration) {
        return Jwts.builder()
                .claims(claims)
                .expiration(expiration)
                .signWith(JwtUtil.getKey())
                .compact();
    }

    public static Jwt<?, ?> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(JwtUtil.getKey())
                .build()
                .parse(token);
    }
}
