package com.lms.search.controller;

import com.lms.search.model.SearchIndex;
import com.lms.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @PostMapping
    public SearchIndex add(@RequestBody SearchIndex index) {
        return service.addCourse(index);
    }

    @GetMapping("/{keyword}")
    public List<SearchIndex> search(@PathVariable String keyword) {
        return service.search(keyword);
    }
}
