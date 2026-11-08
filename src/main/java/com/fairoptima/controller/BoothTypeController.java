package com.fairoptima.controller;

import com.fairoptima.dto.request.BoothTypePatchRequest;
import com.fairoptima.dto.request.BoothTypeRequest;
import com.fairoptima.dto.response.BoothTypeResponse;
import com.fairoptima.service.BoothTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booth-types")
@Tag(name = "Booth Type Management", description = "Stant tipi yonetimi endpoint'leri")
public class BoothTypeController {

    private final BoothTypeService boothTypeService;

    public BoothTypeController(BoothTypeService boothTypeService) {
        this.boothTypeService = boothTypeService;
    }

    @GetMapping
    @Operation(summary = "Stant tiplerini listele")
    public List<BoothTypeResponse> getAll() {
        return boothTypeService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile stant tipi getir")
    public BoothTypeResponse getById(@PathVariable Long id) {
        return boothTypeService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Yeni stant tipi olustur", description = "Ad benzersiz olmali")
    public ResponseEntity<BoothTypeResponse> create(@Valid @RequestBody BoothTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boothTypeService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Stant tipini tamamen guncelle (PUT)")
    public BoothTypeResponse update(@PathVariable Long id, @Valid @RequestBody BoothTypeRequest request) {
        return boothTypeService.update(id, request);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Stant tipini kismen guncelle (PATCH)")
    public BoothTypeResponse patch(@PathVariable Long id, @Valid @RequestBody BoothTypePatchRequest request) {
        return boothTypeService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Stant tipini sil", description = "Stanti olan tip silinemez")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boothTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}