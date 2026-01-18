package com.lms.batch.controller;

import com.lms.batch.entity.Branch;
import com.lms.batch.service.BranchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/branches")
public class AdminBranchController {

    private final BranchService branchService;

    public AdminBranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    public Branch create(@RequestBody Branch branch) {
        return branchService.create(branch.getName(), branch.getCity());
    }

    @GetMapping
    public List<Branch> getAll() {
        return branchService.getAll();
    }
    @PutMapping("/{id}")
    public Branch update(
            @PathVariable Long id,
            @RequestBody Branch branch
    ) {
        return branchService.update(id, branch.getName(), branch.getCity());
    }

}
