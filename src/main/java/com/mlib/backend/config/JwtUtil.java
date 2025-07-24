package com.mlib.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Create a SecretKey from the secret string
    private SecretKey getSigningKey() {
        // JJWT requires at least a 256-bit key for HS512
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** Generate a JWT token */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /** Extract username (subject) from token */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()                       // ← use parserBuilder()
                    .setSigningKey(getSigningKey())          // ← set your key
                    .build()                                // ← build it
                    .parseClaimsJws(token)                  // ← parse JWS
                    .getBody();                              // ← extract Claims

            return claims.getSubject();
        } catch (Exception e) {
            // optionally log e.getMessage()
            return null;
        }
    }

    /** Validate token matches given username and is not expired */
    public boolean validateToken(String token, String username) {
        String extractedUsername = getUsernameFromToken(token);
        return extractedUsername != null
                && extractedUsername.equals(username)
                && !isTokenExpired(token);
    }

    /** Check if the token’s expiration date is before now */
    private boolean isTokenExpired(String token) {
        try {
            Date expirationDate = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return expirationDate.before(new Date());
        } catch (Exception e) {
            // treat unparseable tokens as expired
            return true;
        }
    }
}
