package com.lms.batch.service;

import com.lms.batch.entity.Branch;
import com.lms.batch.repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    private final BranchRepository repository;

    public BranchService(BranchRepository repository) {
        this.repository = repository;
    }

    public Branch create(String name, String city) {
        return repository.save(new Branch(name, city));
    }

    public List<Branch> getAll() {
        return repository.findAll();
    }
    public Branch update(Long id, String name, String city) {
        Branch branch = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        branch.setName(name);
        branch.setCity(city);

        return repository.save(branch);
    }

}
