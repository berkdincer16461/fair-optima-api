package com.fairoptima.controller;

import com.fairoptima.dto.request.BoothRequest;
import com.fairoptima.dto.response.BoothResponse;
import com.fairoptima.service.BoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booths")
@Tag(name = "Booth Management", description = "Stant yonetimi endpoint'leri")
public class BoothController {

    private final BoothService boothService;

    public BoothController(BoothService boothService) {
        this.boothService = boothService;
    }

    @PostMapping
    @Operation(summary = "Yeni stant olustur")
    public ResponseEntity<BoothResponse> create(@Valid @RequestBody BoothRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boothService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile stant getir")
    public ResponseEntity<BoothResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(boothService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Tum stantlari listele")
    public ResponseEntity<List<BoothResponse>> getAll() {
        return ResponseEntity.ok(boothService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Stant bilgilerini tam guncelle")
    public ResponseEntity<BoothResponse> update(@PathVariable Long id, @Valid @RequestBody BoothRequest request) {
        return ResponseEntity.ok(boothService.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Stant bilgilerini kismi guncelle (PATCH)")
    public ResponseEntity<BoothResponse> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(boothService.patch(id, updates));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Stant sil")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boothService.delete(id);
        return ResponseEntity.noContent().build();
    }
}