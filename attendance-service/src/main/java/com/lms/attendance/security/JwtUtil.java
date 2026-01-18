package com.lms.attendance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtUtil {

    private static final String SECRET =
            "mysupersecretkeymysupersecretkey";

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractEmail(String token) {
        return extractAllClaims(token).getSubject(); // email
    }

    public static String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
