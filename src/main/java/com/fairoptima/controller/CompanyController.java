package com.fairoptima.controller;

import com.fairoptima.dto.CompanyPatchRequest;
import com.fairoptima.dto.CompanyRequest;
import com.fairoptima.dto.CompanyResponse;
import com.fairoptima.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<CompanyResponse> getAll(@RequestParam(required = false) String sector) {
        return companyService.getAll(sector);
    }

    @GetMapping("/{id}")
    public CompanyResponse getById(@PathVariable Long id) {
        return companyService.getById(id);
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return companyService.update(id, request);
    }

    @PatchMapping("/{id}")
    public CompanyResponse patch(@PathVariable Long id, @Valid @RequestBody CompanyPatchRequest request) {
        return companyService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}