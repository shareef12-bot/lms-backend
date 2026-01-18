////package com.lms.gateway.security;
////
////import org.springframework.cloud.gateway.filter.GlobalFilter;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.http.HttpHeaders;
////import org.springframework.http.HttpStatus;
////
////@Configuration
////public class GatewaySecurityConfig {
////
////    private final JwtUtil jwtUtil;
////
////    public GatewaySecurityConfig(JwtUtil jwtUtil) {
////        this.jwtUtil = jwtUtil;
////    }
////
////    @Bean
////    public GlobalFilter authenticationFilter() {
////        return (exchange, chain) -> {
////
////            String path = exchange.getRequest().getURI().getPath();
////
////            // 🔓 PUBLIC ENDPOINTS (NO JWT REQUIRED)
////            if (path.startsWith("/api/auth/google")      // ✅ Google OAuth
////                    || path.startsWith("/api/auth/login") // ✅ Normal login
////                    || path.startsWith("/api/auth/register")
////                    || path.startsWith("/api/auth/forgot-password")
////                    || path.startsWith("/api/auth/reset-password")
////                    || path.startsWith("/api/video/play/")) {
////
////                return chain.filter(exchange);
////            }
////
////            // 🔐 JWT REQUIRED FOR ALL OTHER ROUTES
////            String authHeader =
////                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
////
////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
////                return exchange.getResponse().setComplete();
////            }
////
////            String token = authHeader.substring(7);
////
////            try {
////                // ✅ VALIDATE JWT (signature + expiry)
////                jwtUtil.validateToken(token);
////            } catch (Exception e) {
////                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
////                return exchange.getResponse().setComplete();
////            }
////
////            return chain.filter(exchange);
////        };
////    }
////}
//package com.lms.gateway.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class GatewaySecurityConfig {
//
//    private final JwtUtil jwtUtil;
//    private final SecretKey key;
//
//    public GatewaySecurityConfig(
//            JwtUtil jwtUtil,
//            @Value("${jwt.secret}") String secret
//    ) {
//        this.jwtUtil = jwtUtil;
//        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    @Bean
//    public GlobalFilter authenticationFilter() {
//        return (exchange, chain) -> {
//
//            String path = exchange.getRequest().getURI().getPath();
//
//            // 🔓 PUBLIC ENDPOINTS
//            if (path.startsWith("/api/auth/google")
//                    || path.startsWith("/api/auth/login")
//                    || path.startsWith("/api/auth/register")
//                    || path.startsWith("/api/auth/forgot-password")
//                    || path.startsWith("/api/auth/reset-password")
//                    || path.startsWith("/api/video/play/")) {
//
//                return chain.filter(exchange);
//            }
//
//            // 🔐 JWT REQUIRED
//            String authHeader =
//                    exchange.getRequest().getHeaders()
//                            .getFirst(HttpHeaders.AUTHORIZATION);
//
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                exchange.getResponse()
//                        .setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//
//            String token = authHeader.substring(7);
//
//            try {
//                jwtUtil.validateToken(token);
//            } catch (Exception e) {
//                exchange.getResponse()
//                        .setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//
//            // 🔍 Extract role
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String role = claims.get("role", String.class);
//
//            // ================= STUDENT SERVICE =================
//            if (path.startsWith("/api/students")) {
//                if (!"ADMIN".equalsIgnoreCase(role)) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//            }
//
//            // ================= ENROLLMENT SERVICE =================
//            if (path.startsWith("/api/enrollments")) {
//
//                if ("STUDENT".equalsIgnoreCase(role)
//                        && !path.startsWith("/api/enrollments/student")) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//
//                if (!"ADMIN".equalsIgnoreCase(role)
//                        && !"STUDENT".equalsIgnoreCase(role)) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//            }
//            
//
//            // ================= PROGRESS SERVICE (✅ ADDED) =================
//            if (path.startsWith("/api/progress")) {
//
//                // STUDENT: only allowed to view own progress
//                if ("STUDENT".equalsIgnoreCase(role)
//                        && !path.startsWith("/api/progress/student")) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//
//                // ADMIN: full access
//                if (!"ADMIN".equalsIgnoreCase(role)
//                        && !"STUDENT".equalsIgnoreCase(role)) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//            }
//         // ================= ASSESSMENT SERVICE =================
//            if (path.startsWith("/api/quizzes")
//                    || path.startsWith("/api/questions")
//                    || path.startsWith("/api/options")
//                    || path.startsWith("/api/attempts")) {
//
//                // STUDENT: only attempts & view quizzes
//                if ("STUDENT".equalsIgnoreCase(role)) {
//
//                    if (path.startsWith("/api/questions")
//                            || path.startsWith("/api/options")) {
//                        exchange.getResponse()
//                                .setStatusCode(HttpStatus.FORBIDDEN);
//                        return exchange.getResponse().setComplete();
//                    }
//                }
//
//                // TRAINER: cannot delete attempts
//                if ("TRAINER".equalsIgnoreCase(role)
//                        && path.startsWith("/api/attempts")) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//
//                // ADMIN / TRAINER / STUDENT allowed
//                if (!"ADMIN".equalsIgnoreCase(role)
//                        && !"TRAINER".equalsIgnoreCase(role)
//                        && !"STUDENT".equalsIgnoreCase(role)) {
//
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//            }
//         // ================= PAYMENT SERVICE =================
//            if (path.startsWith("/api/payment")
//                    || path.startsWith("/api/refund")) {
//
//                // STUDENT: cannot refund
//                if ("STUDENT".equalsIgnoreCase(role)
//                        && path.startsWith("/api/refund")) {
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//
//                // Only ADMIN & STUDENT
//                if (!"ADMIN".equalsIgnoreCase(role)
//                        && !"STUDENT".equalsIgnoreCase(role)) {
//
//                    exchange.getResponse()
//                            .setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//            }
//
//            return chain.filter(exchange);
//            
//        };
//    }
//}
//
package com.lms.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class GatewaySecurityConfig {

    private final JwtUtil jwtUtil;
    private final SecretKey key;

    public GatewaySecurityConfig(
            JwtUtil jwtUtil,
            @Value("${jwt.secret}") String secret
    ) {
        this.jwtUtil = jwtUtil;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public GlobalFilter authenticationFilter() {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();

            // ================= MISSING LOGIC #1 =================
            // ✅ Allow CORS preflight
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }
             
            // 🔓 PUBLIC ENDPOINTS
            if (path.startsWith("/api/auth/google")
                    || path.startsWith("/api/auth/login")
                    || path.startsWith("/api/auth/register")
                    || path.startsWith("/api/auth/forgot-password")
                    || path.startsWith("/api/auth/reset-password")
                    || path.startsWith("/api/courses/")
                    || path.startsWith("/api/content/student/course/")
                    || path.startsWith("/api/content/course/")
                    ||path.startsWith("/api/video/play/")
                    ||path.startsWith("/api/files/view/")) {

                return chain.filter(exchange);
            }

            // 🔐 JWT REQUIRED
            String authHeader =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                jwtUtil.validateToken(token);
            } catch (Exception e) {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = claims.get("role", String.class);

            // ================= MISSING LOGIC #2 =================
            // ✅ SEARCH SERVICE (no role restriction)
            if (path.startsWith("/api/search")) {
                return chain.filter(exchange);
            }

            // ================= STUDENT SERVICE =================
            if (path.startsWith("/api/students")) {

                // Allow ADMIN and TRAINER
                if (!"ADMIN".equalsIgnoreCase(role)
                        && !"TRAINER".equalsIgnoreCase(role)) {

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            
         // ================= TRAINER SERVICE (✅ MISSING – ADD THIS) =================
            if (path.startsWith("/api/trainers")) {

                // Only ADMIN can manage trainers
                if (!"ADMIN".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
            
            
         // ================= VIDEO SERVICE =================
            if (path.startsWith("/api/video")) {

                // ❌ Block students from uploading
                if (path.startsWith("/api/video/upload")) {
                    if (!"TRAINER".equalsIgnoreCase(role)
                            && !"ADMIN".equalsIgnoreCase(role)) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }

                // ✅ Allow viewing/listing for STUDENT, TRAINER, ADMIN
                if (!"ADMIN".equalsIgnoreCase(role)
                        && !"TRAINER".equalsIgnoreCase(role)
                        && !"STUDENT".equalsIgnoreCase(role)) {

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
                
                if (path.startsWith("/api/video") && exchange.getRequest().getMethod() == HttpMethod.DELETE) {
                    if (!"TRAINER".equalsIgnoreCase(role)
                            && !"ADMIN".equalsIgnoreCase(role)) {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }

            }



            // ================= ENROLLMENT SERVICE =================
            if (path.startsWith("/api/enrollments")) {

                if ("STUDENT".equalsIgnoreCase(role)
                        && !path.startsWith("/api/enrollments/student")) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                if (!"ADMIN".equalsIgnoreCase(role)
                        && !"STUDENT".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            // ================= PROGRESS SERVICE =================
            if (path.startsWith("/api/progress")) {

                if ("STUDENT".equalsIgnoreCase(role)
                        && !path.startsWith("/api/progress/student")) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                if (!"ADMIN".equalsIgnoreCase(role)
                        && !"STUDENT".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
            
         // ================= FILE SERVICE =================
            if (path.startsWith("/api/files")) {

                // ❌ STUDENT CANNOT UPLOAD
                if (path.startsWith("/api/files/upload")) {
                    if (!"ADMIN".equalsIgnoreCase(role)
                            && !"TRAINER".equalsIgnoreCase(role)) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }

                // ❌ STUDENT CANNOT DELETE
                if (exchange.getRequest().getMethod() == HttpMethod.DELETE) {
                    if (!"ADMIN".equalsIgnoreCase(role)
                            && !"TRAINER".equalsIgnoreCase(role)) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }

                // ✅ LIST + DOWNLOAD allowed for ADMIN / TRAINER / STUDENT
                if (!"ADMIN".equalsIgnoreCase(role)
                        && !"TRAINER".equalsIgnoreCase(role)
                        && !"STUDENT".equalsIgnoreCase(role)) {

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

         // ================= ASSESSMENT SERVICE =================
         // ================= ATTEMPT CHECK =================
         // ================= ATTEMPTS =================

         // ✅ Student can check if he already attempted
         if (path.startsWith("/api/attempts/has-attempted")) {

             if ("STUDENT".equalsIgnoreCase(role)
                     && exchange.getRequest().getMethod() == HttpMethod.GET) {
                 return chain.filter(exchange);
             }

             exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
             return exchange.getResponse().setComplete();
         }

         // ✅ Student can submit attempt
         if (path.startsWith("/api/attempts/submit")) {

             if ("STUDENT".equalsIgnoreCase(role)
                     && exchange.getRequest().getMethod() == HttpMethod.POST) {
                 return chain.filter(exchange);
             }

             exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
             return exchange.getResponse().setComplete();
         }
         if (path.equals("/api/attempts/my")) {

        	    if ("STUDENT".equalsIgnoreCase(role)
        	            && exchange.getRequest().getMethod() == HttpMethod.GET) {
        	        return chain.filter(exchange);
        	    }

        	    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        	    return exchange.getResponse().setComplete();
        	}

         // 🔐 Trainers/Admins can view attempts
         if (path.startsWith("/api/attempts")) {

             if ("TRAINER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
                 return chain.filter(exchange);
             }

             exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
             return exchange.getResponse().setComplete();
         }
     
            // ================= QUIZZES =================
         // ================= QUIZ READ =================
            if (path.matches("/api/quizzes(/.*)?")) {

                // STUDENT can READ quizzes
                if ("STUDENT".equalsIgnoreCase(role)
                        && exchange.getRequest().getMethod() == HttpMethod.GET) {
                    return chain.filter(exchange);
                }

                // ADMIN & TRAINER full access
                if ("ADMIN".equalsIgnoreCase(role)
                        || "TRAINER".equalsIgnoreCase(role)) {
                    return chain.filter(exchange);
                }

                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }


            // ================= QUESTIONS / OPTIONS =================
            if (path.startsWith("/api/questions")
                    || path.startsWith("/api/options")) {

                // ❌ Student blocked
                if ("STUDENT".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                // ✅ Admin & Trainer
                if ("ADMIN".equalsIgnoreCase(role)
                        || "TRAINER".equalsIgnoreCase(role)) {
                    return chain.filter(exchange);
                }

                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            
            
         // ================= BATCH SERVICE =================
         // ================= ADMIN BRANCH SERVICE (FINAL FIX) =================
            if (path.startsWith("/api/admin/branches")) {

                if (!"ADMIN".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                // 🔥 VERY IMPORTANT: STOP ALL FURTHER CHECKS
                return chain.filter(exchange);
            }

            
            

         // ADMIN batch management
         
         // ================= ADMIN BATCH SERVICE =================
            if (path.startsWith("/api/admin/batches")) {

                if (!"ADMIN".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                // 🔥 STOP HERE — allow ADMIN
                return chain.filter(exchange);
            }

         // TRAINER batch access
         if (path.startsWith("/api/trainer/batches")
                 || path.startsWith("/api/trainer/batch-reports")) {

             if (!"TRAINER".equalsIgnoreCase(role)
                     && !"ADMIN".equalsIgnoreCase(role)) {

                 exchange.getResponse()
                         .setStatusCode(HttpStatus.FORBIDDEN);
                 return exchange.getResponse().setComplete();
             }
         }
         
     

      // ================= TRAINER ROOT ACCESS (FIX) =================
         if (path.startsWith("/api/trainer/")) {

             if (!"TRAINER".equalsIgnoreCase(role)
                     && !"ADMIN".equalsIgnoreCase(role)) {

                 exchange.getResponse()
                         .setStatusCode(HttpStatus.FORBIDDEN);
                 return exchange.getResponse().setComplete();
             }

             // ✅ trainer/admin allowed → continue
             return chain.filter(exchange);
         }

         // STUDENT batch access
         if (path.startsWith("/api/student/batch")) {

             if (!"STUDENT".equalsIgnoreCase(role)) {
                 exchange.getResponse()
                         .setStatusCode(HttpStatus.FORBIDDEN);
                 return exchange.getResponse().setComplete();
             }
         }

            
         // ================= ATTENDANCE SERVICE =================

         // 🧑‍🏫 Trainer Attendance APIs
         if (path.startsWith("/api/trainer/attendance")) {

             if ("TRAINER".equalsIgnoreCase(role)
                     || "ADMIN".equalsIgnoreCase(role)) {
                 return chain.filter(exchange);
             }

             exchange.getResponse()
                     .setStatusCode(HttpStatus.FORBIDDEN);
             return exchange.getResponse().setComplete();
         }

         // 🧑‍🎓 Student Attendance APIs
         if (path.startsWith("/api/student/attendance")) {

             if ("STUDENT".equalsIgnoreCase(role)) {
                 return chain.filter(exchange);
             }

             exchange.getResponse()
                     .setStatusCode(HttpStatus.FORBIDDEN);
             return exchange.getResponse().setComplete();
         }


       
            // ================= PAYMENT SERVICE =================
            if (path.startsWith("/api/payment")
                    || path.startsWith("/api/refund")) {
 
                if ("STUDENT".equalsIgnoreCase(role)
                        && path.startsWith("/api/refund")) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                if (!"ADMIN".equalsIgnoreCase(role)
                        && !"STUDENT".equalsIgnoreCase(role)) {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange);
        };
    }
}

