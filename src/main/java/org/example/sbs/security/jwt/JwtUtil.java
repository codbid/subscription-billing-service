package org.example.sbs.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.*;

@Component
public class JwtUtil {
    private final String SECRET = "38ce45d952665e91887946aaaceb923217721f6a66586027962fc14d5b50e9143f418e34855910b0aad5a66980619be8d4f1ed65bcd7abda9c9978ad18050d077f58fb12a4f3798e03d023e5e313838cbfd746bcaeba89f11cb6db75f4e46c770fd6dfe522a8165fcbd65c318f1719779e95bd8bd991ee34a5c76d0332337b90042067da776b95313c0333e837458585b461bc37c098edecbd2826dad6ebcb0c63fb1775818bb026bc67e6f24ad8d3e655f6eafea6f266a397e9f447b4064f7a22483bd8fc1268b830fbe810657fd552738662493894bd833dced395da5af03900f82ba4c4ecdcf1bda9ecdc7bc1f13366a4fc13eacf337fc98541da7bee80d8";

    public String generateToken(String login, Long userId, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("roles", roles);
        claims.put("login", login);
        return Jwts.builder()
                .setSubject(login)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("login", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
