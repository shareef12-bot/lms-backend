package com.lms.auth.security;

import com.lms.auth.model.Role;
import com.lms.auth.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "mysupersecretkeymysupersecretkey";
    private static final long EXPIRATION_MS = 60 * 60 * 1000; // 1 hour

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // ✅ EXISTING METHOD (UNCHANGED)
    public String generateToken(String email, Role role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ NEW METHOD (ONLY ADDITION – FIXES ISSUE)
    public String generateToken(User user) {
        return generateToken(user.getEmail(), user.getRole());
    }
}
