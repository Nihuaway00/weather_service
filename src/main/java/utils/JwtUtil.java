package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
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

    public static Jwt<?, ?> parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(JwtUtil.getKey())
                .build()
                .parse(token);
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(JwtUtil.getKey())
                    .build()
                    .parse(token);

            return true;
        } catch (SignatureException e) {
            // Некорректная подпись
            System.out.println("Некорректная подпись");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Истек срок действия токена
            System.out.println("Истек срок действия токена");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // Некорректный JWT
            System.out.println("Некорректный JWT");
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // Неподдерживаемый JWT
            System.out.println("Неподдерживаемый JWT");
        } catch (IllegalArgumentException e) {
            // Пустой или некорректный токен
            System.out.println("Пустой или некорректный токен.");
        }

        return false;
    }

    public static String parsePayloadToJson(String payload) {
        payload = payload.replace("{", "{\"");
        payload = payload.replace("}", "\"}");
        payload = payload.replace(", ", "\", \"");
        payload = payload.replace("=", "\": \"");
        return payload;
    }
}
