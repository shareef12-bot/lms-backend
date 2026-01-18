package com.lms.batch.controller;

import com.lms.batch.dto.AdminBatchResponse;
import com.lms.batch.dto.CreateBatchRequest;
import com.lms.batch.entity.Batch;
import com.lms.batch.service.BatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/batches")
public class AdminBatchController {

    private final BatchService batchService;

    public AdminBatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    // ✅ NO CHANGE
    @PostMapping
    public Batch createBatch(@RequestBody CreateBatchRequest request) {
        return batchService.createBatch(request);
    }

    // ✅ ONLY THIS FIXED
    @GetMapping
    public List<AdminBatchResponse> getAllBatches() {
        return batchService.getAllBatches();
    }
}
