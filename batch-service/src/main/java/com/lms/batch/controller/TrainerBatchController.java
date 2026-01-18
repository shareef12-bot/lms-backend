//package com.lms.batch.controller;
//
//import com.lms.batch.entity.Batch;
//import com.lms.batch.service.BatchService;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/trainer/batches")
//public class TrainerBatchController {
//
//    private final BatchService batchService;
//
//    public TrainerBatchController(BatchService batchService) {
//        this.batchService = batchService;
//    }
//
//    @GetMapping
//    public List<Batch> myBatches(Authentication auth) {
//        Long trainerId = 1L; // extract from JWT later
//        return batchService.getTrainerBatches(trainerId);
//    }
//}



//package com.lms.batch.controller;
//
//import com.lms.batch.dto.BatchReportResponse;
//import com.lms.batch.entity.Batch;
//import com.lms.batch.security.JwtUtil;
//import com.lms.batch.service.BatchService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/trainer")
//public class TrainerBatchController {
//
//    private final BatchService batchService;
//    private final JwtUtil jwtUtil;
//
//    public TrainerBatchController(BatchService batchService, JwtUtil jwtUtil) {
//        this.batchService = batchService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    // ✅ Trainer → My Batches
//    @GetMapping("/batches")
//    public List<Batch> getTrainerBatches(HttpServletRequest request) {
//
//        String header = request.getHeader("Authorization");
//        String token = header.substring(7);
//
//        Long trainerId = jwtUtil.extractUserId(token);
//
//        return batchService.getTrainerBatches(trainerId);
//    }
//
//    // ✅ Trainer → Batch Reports
//    @GetMapping("/batch-reports")
//    public List<BatchReportResponse> getBatchReports(HttpServletRequest request) {
//
//        String header = request.getHeader("Authorization");
//        String token = header.substring(7);
//
//        Long trainerId = jwtUtil.extractUserId(token);
//
//        return batchService.getBatchReports(trainerId);
//    }
//   
//}


package com.lms.batch.controller;

import com.lms.batch.dto.BatchReportResponse;
import com.lms.batch.dto.BatchResponseDTO;
import com.lms.batch.security.JwtUtil;
import com.lms.batch.service.BatchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainer")
public class TrainerBatchController {

    private final BatchService batchService;
    private final JwtUtil jwtUtil;

    public TrainerBatchController(BatchService batchService, JwtUtil jwtUtil) {
        this.batchService = batchService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Trainer → My Batches
    @GetMapping("/batches")
    public List<BatchResponseDTO> getTrainerBatches(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        Long trainerId = jwtUtil.extractUserId(token);

        return batchService.getTrainerBatchDTOs(trainerId);
    }

    // ✅ Trainer → Batch Reports
    @GetMapping("/batch-reports")
    public List<BatchReportResponse> getBatchReports(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        Long trainerId = jwtUtil.extractUserId(token);

        return batchService.getBatchReports(trainerId);
    }
}
