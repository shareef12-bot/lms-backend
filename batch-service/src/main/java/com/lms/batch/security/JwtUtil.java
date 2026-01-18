//package com.lms.batch.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class JwtUtil {
//
//    // 🔴 SAME SECRET USED IN AUTH SERVICE
//    private static final String SECRET =
//            "mysupersecretkeymysupersecretkey";
//
//    private final SecretKey key =
//            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//
//    public Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public String extractEmail(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    public String extractRole(String token) {
//        return extractAllClaims(token).get("role", String.class);
//    }
//    public Long extractUserId(String token) {
//        return Long.parseLong(extractClaim(token, "userId"));
//    }
//
//
//    public boolean validateToken(String token) {
//        try {
//            extractAllClaims(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
package com.lms.batch.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    // 🔴 SAME SECRET USED IN AUTH SERVICE
    private static final String SECRET =
            "mysupersecretkeymysupersecretkey";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ✅ ADDED (MISSING)
    public String extractClaim(String token, String claimKey) {
        Object value = extractAllClaims(token).get(claimKey);
        return value == null ? null : value.toString();
    }

    public Long extractUserId(String token) {
        String userId = extractClaim(token, "userId");
        return userId == null ? null : Long.parseLong(userId);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

