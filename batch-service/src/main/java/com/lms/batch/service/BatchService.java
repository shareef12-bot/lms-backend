//package com.lms.batch.service;
//
//import com.lms.batch.dto.CreateBatchRequest;
//import com.lms.batch.entity.Batch;
//import com.lms.batch.repository.BatchRepository;
//import com.lms.batch.util.BatchCodeGenerator;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class BatchService {
//
//    private final BatchRepository batchRepository;
//
//    public BatchService(BatchRepository batchRepository) {
//        this.batchRepository = batchRepository;
//    }
//
//    public Batch createBatch(CreateBatchRequest request) {
//
//        Batch batch = new Batch();
//        batch.setBatchCode(BatchCodeGenerator.generate());
//        batch.setName(request.getBatchName());
//        batch.setBranchId(request.getBranchId());
//        batch.setCourseId(request.getCourseId());
//        batch.setTrainerId(request.getTrainerId());
//        batch.setStartDate(request.getStartDate());
//        batch.setEndDate(request.getEndDate());
//
//        LocalDate today = LocalDate.now();
//        if (today.isBefore(batch.getStartDate())) batch.setStatus("UPCOMING");
//        else if (today.isAfter(batch.getEndDate())) batch.setStatus("COMPLETED");
//        else batch.setStatus("RUNNING");
//
//        return batchRepository.save(batch);
//    }
//
//    public List<Batch> getTrainerBatches(Long trainerId) {
//        return batchRepository.findByTrainerId(trainerId);
//    }
//    public List<Batch> getAllBatches() {
//        return batchRepository.findAll();
//    }
//}


package com.lms.batch.service;

import com.lms.batch.dto.AdminBatchResponse;

import com.lms.batch.dto.CreateBatchRequest;
import com.lms.batch.entity.Batch;
import com.lms.batch.repository.BatchRepository;
import com.lms.batch.util.BatchCodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.lms.batch.dto.BatchReportResponse;
import java.util.ArrayList;
import com.lms.batch.dto.BatchReportResponse;
import com.lms.batch.dto.BatchResponseDTO;
import java.util.stream.Collectors;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    // ================= EXISTING (NO CHANGE) =================
    public Batch createBatch(CreateBatchRequest request) {

        Batch batch = new Batch();
        batch.setBatchCode(BatchCodeGenerator.generate());
        batch.setName(request.getName());
        batch.setBranchId(request.getBranchId());
        batch.setCourseId(request.getCourseId());
        batch.setTrainerId(request.getTrainerId());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());

        LocalDate today = LocalDate.now();
        if (today.isBefore(batch.getStartDate())) batch.setStatus("UPCOMING");
        else if (today.isAfter(batch.getEndDate())) batch.setStatus("COMPLETED");
        else batch.setStatus("RUNNING");

        return batchRepository.save(batch);
    }

    public List<Batch> getTrainerBatches(Long trainerId) {
        return batchRepository.findByTrainerId(trainerId);
    }

    // ================= 🔥 FIXED METHOD =================
    public List<AdminBatchResponse> getAllBatches() {

        return batchRepository.findAll().stream().map(batch -> {

            AdminBatchResponse dto = new AdminBatchResponse();

            dto.setId(batch.getId());
            dto.setName(batch.getName());
            dto.setStartDate(batch.getStartDate());
            dto.setEndDate(batch.getEndDate());

            dto.setCourseId(batch.getCourseId());
            dto.setTrainerId(batch.getTrainerId());

            // TEMP display values (until service integration)
            dto.setCourseName("Course #" + batch.getCourseId());
            dto.setTrainerName("Trainer #" + batch.getTrainerId());

            return dto;
        }).toList();
    }
    
    public List<BatchReportResponse> getBatchReports(Long trainerId) {

        return batchRepository.findByTrainerId(trainerId).stream().map(batch -> {

            BatchReportResponse dto = new BatchReportResponse();
            dto.setBatchName(batch.getName());

            dto.setStudents(0);
            dto.setAvgScore(0);
            dto.setCompletion(0);

            return dto;

        }).toList();
    }


    public List<BatchResponseDTO> getTrainerBatchDTOs(Long trainerId) {

        return batchRepository.findByTrainerId(trainerId).stream().map(batch -> {

            BatchResponseDTO dto = new BatchResponseDTO();
            dto.setBatchCode(batch.getBatchCode());
            dto.setName(batch.getName());
            dto.setStatus(batch.getStatus());

            // 🔥 for now you don't have students count table mapping
            dto.setStudents(0);

            return dto;

        }).toList();
    }

    public List<BatchReportResponse> getBatchReportsForTrainer(Long trainerId) {

        return batchRepository.findByTrainerId(trainerId).stream().map(batch -> {

            BatchReportResponse dto = new BatchReportResponse();
            dto.setBatchName(batch.getName());

            dto.setStudents(0);
            dto.setAvgScore(0);
            dto.setCompletion(0);

            return dto;

        }).toList();
    }

}
