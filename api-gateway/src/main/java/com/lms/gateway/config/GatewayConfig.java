//
//
//package com.lms.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
//
//        return builder.routes()
//
//                // ================= AUTH =================
//                .route("auth-service", r -> r.path("/api/auth/**")
//                        .uri("http://localhost:8081"))
//
//                // ================= USER =================
//                .route("user-service", r -> r.path("/api/users/**")
//                        .uri("http://localhost:8082"))
//
//                // ================= COURSE =================
//                .route("course-service", r -> r.path("/api/courses/**")
//                        .uri("http://localhost:8083"))
//
//                // ================= CONTENT =================
//                .route("content-service", r -> r.path("/api/content/**")
//                        .uri("http://localhost:8083"))
//
//                // ================= VIDEO =================
//                .route("video-service", r -> r.path("/api/video/**")
//                        .uri("http://localhost:8084"))
//
//                // ================= NOTIFICATION =================
//                .route("notification-service", r -> r.path("/api/notification/**")
//                        .uri("http://localhost:8085"))
//
//                // ================= ANALYTICS =================
//                .route("analytics-service", r -> r.path("/api/analytics/**")
//                        .uri("http://localhost:8086"))
//
//                // ================= ASSESSMENT =================
//                .route("assessment-service", r -> r.path(
//                        "/api/quizzes/**",
//                        "/api/questions/**",
//                        "/api/options/**",
//                        "/api/attempts/**"
//                ).uri("http://localhost:8087"))
//
//                // ================= ENROLLMENT =================
//                .route("enrollment-service", r -> r.path("/api/enrollments/**")
//                        .uri("http://localhost:8088"))
//
//                // ================= PROGRESS =================
//                .route("progress-service", r -> r.path("/api/progress/**")
//                        .uri("http://localhost:8089"))
//
//                // ================= SEARCH =================
//                .route("search-service", r -> r.path("/api/search/**")
//                        .uri("http://localhost:8090"))
//
//                // ================= PAYMENT =================
//                .route("payment-service", r -> r.path(
//                        "/api/payment/**",
//                        "/api/refund/**"
//                ).uri("http://localhost:8091"))
//
//                // ================= FILE SERVICE =================
//                .route("file-service", r -> r.path("/api/files/**")
//                        .uri("http://localhost:8092"))
//
//                // ================= STUDENT + TRAINER SERVICE (✅ ONLY ADDITION) =================
//                .route("student-service", r -> r.path(
//                        "/api/students/**",
//                        "/api/trainers/**"   // ✅ ADDED (nothing else changed)
//                ).uri("http://localhost:8093"))
//                // ================= ATTENDANCE SERVICE (✅ NEW) =================
//                .route("attendance-service", r -> r.path(
//                 "/api/trainer/attendance/**",
//                "/api/student/attendance/**"
//                ).uri("http://localhost:8094"))
//
//                // ================= BATCH SERVICE (✅ NEW) =================
//                .route("batch-service", r -> r.path(
//                        "/api/admin/batches/**",
//                        "/api/trainer/batches/**",
//                        "/api/admin/branches/**", 
//                        "/api/trainer/batch-reports/**",
//                        "/api/student/batch/**",
//                        "/api/admin/branches/**"
//                ).uri("http://localhost:8095"))
//                
//                .build();
//        
//    }
//}
//


package com.lms.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {

    @Value("${services.auth}")
    private String authService;

    @Value("${services.user}")
    private String userService;

    @Value("${services.course}")
    private String courseService;

    @Value("${services.content}")
    private String contentService;

    @Value("${services.video}")
    private String videoService;

    @Value("${services.notification}")
    private String notificationService;

    @Value("${services.analytics}")
    private String analyticsService;

    @Value("${services.assessment}")
    private String assessmentService;

    @Value("${services.enrollment}")
    private String enrollmentService;

    @Value("${services.progress}")
    private String progressService;

    @Value("${services.search}")
    private String searchService;

    @Value("${services.payment}")
    private String paymentService;

    @Value("${services.file}")
    private String fileService;

    @Value("${services.student}")
    private String studentService;

    @Value("${services.attendance}")
    private String attendanceService;

    @Value("${services.batch}")
    private String batchService;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {

        return builder.routes()

                // ================= AUTH =================
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri(authService))

                // ================= USER =================
                .route("user-service", r -> r.path("/api/users/**")
                        .uri(userService))

                // ================= COURSE =================
                .route("course-service", r -> r.path("/api/courses/**")
                        .uri(courseService))

                // ================= CONTENT =================
                .route("content-service", r -> r.path("/api/content/**")
                        .uri(contentService))

                // ================= VIDEO =================
                .route("video-service", r -> r.path("/api/video/**")
                        .uri(videoService))

                // ================= NOTIFICATION =================
                .route("notification-service", r -> r.path("/api/notification/**")
                        .uri(notificationService))

                // ================= ANALYTICS =================
                .route("analytics-service", r -> r.path("/api/analytics/**")
                        .uri(analyticsService))

                // ================= ASSESSMENT =================
                .route("assessment-service", r -> r.path(
                        "/api/quizzes/**",
                        "/api/questions/**",
                        "/api/options/**",
                        "/api/attempts/**"
                ).uri(assessmentService))

                // ================= ENROLLMENT =================
                .route("enrollment-service", r -> r.path("/api/enrollments/**")
                        .uri(enrollmentService))

                // ================= PROGRESS =================
                .route("progress-service", r -> r.path("/api/progress/**")
                        .uri(progressService))

                // ================= SEARCH =================
                .route("search-service", r -> r.path("/api/search/**")
                        .uri(searchService))

                // ================= PAYMENT =================
                .route("payment-service", r -> r.path(
                        "/api/payment/**",
                        "/api/refund/**"
                ).uri(paymentService))

                // ================= FILE SERVICE =================
                .route("file-service", r -> r.path("/api/files/**")
                        .uri(fileService))

                // ================= STUDENT + TRAINER SERVICE (✅ ONLY ADDITION) =================
                .route("student-service", r -> r.path(
                        "/api/students/**",
                        "/api/trainers/**"   // ✅ ADDED (nothing else changed)
                ).uri(studentService))

                // ================= ATTENDANCE SERVICE (✅ NEW) =================
                .route("attendance-service", r -> r.path(
                        "/api/trainer/attendance/**",
                        "/api/student/attendance/**"
                ).uri(attendanceService))

                // ================= BATCH SERVICE (✅ NEW) =================
                .route("batch-service", r -> r.path(
                        "/api/admin/batches/**",
                        "/api/trainer/batches/**",
                        "/api/admin/branches/**",
                        "/api/trainer/batch-reports/**",
                        "/api/student/batch/**",
                        "/api/admin/branches/**"
                ).uri(batchService))

                .build();
    }
}
