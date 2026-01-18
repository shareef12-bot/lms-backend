package com.lms.assessment.controller;
import com.lms.assessment.dto.CreateOptionRequest;
import com.lms.assessment.model.Option;
import com.lms.assessment.service.OptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public Option addOption(@RequestBody CreateOptionRequest req) {
        return optionService.addOption(req);
    }
}
