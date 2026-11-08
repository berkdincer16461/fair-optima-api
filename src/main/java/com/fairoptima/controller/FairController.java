package com.fairoptima.controller;

import com.fairoptima.dto.request.FairPatchRequest;
import com.fairoptima.dto.request.FairRequest;
import com.fairoptima.dto.response.BoothResponse;
import com.fairoptima.dto.response.FairResponse;
import com.fairoptima.entity.enums.FairStatus;
import com.fairoptima.service.BoothService;
import com.fairoptima.service.FairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fairs")
@Tag(name = "Fair Management", description = "Fuar yonetimi endpoint'leri")
public class FairController {

    private final FairService fairService;
    private final BoothService boothService;

    public FairController(FairService fairService, BoothService boothService) {
        this.fairService = fairService;
        this.boothService = boothService;
    }

    @GetMapping
    @Operation(summary = "Fuarlari listele", description = "Istege bagli status filtresi: DRAFT, ACTIVE, COMPLETED, CANCELLED")
    public List<FairResponse> getAll(@RequestParam(required = false) FairStatus status) {
        return fairService.getAll(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile fuar getir")
    public FairResponse getById(@PathVariable Long id) {
        return fairService.getById(id);
    }

    @GetMapping("/{fairId}/booths/available")
    @Operation(summary = "Fuardaki musait stantlari listele")
    public List<BoothResponse> getAvailableBooths(@PathVariable Long fairId) {
        fairService.getById(fairId);
        return boothService.getAvailableByFairId(fairId);
    }

    @PostMapping
    @Operation(summary = "Yeni fuar olustur", description = "status verilmezse DRAFT olur")
    public ResponseEntity<FairResponse> create(@Valid @RequestBody FairRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fairService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Fuari tamamen guncelle (PUT)")
    public FairResponse update(@PathVariable Long id, @Valid @RequestBody FairRequest request) {
        return fairService.update(id, request);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Fuari kismen guncelle (PATCH)")
    public FairResponse patch(@PathVariable Long id, @Valid @RequestBody FairPatchRequest request) {
        return fairService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Fuari sil", description = "Salonu olan fuar silinemez")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fairService.delete(id);
        return ResponseEntity.noContent().build();
    }
}