package com.fairoptima.controller;

import com.fairoptima.dto.request.HallPatchRequest;
import com.fairoptima.dto.request.HallRequest;
import com.fairoptima.dto.response.HallResponse;
import com.fairoptima.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@Tag(name = "Hall Management", description = "Salon yonetimi endpoint'leri")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    @Operation(summary = "Salonlari listele", description = "Istege bagli fairId filtresi")
    public List<HallResponse> getAll(@RequestParam(required = false) Long fairId) {
        return hallService.getAll(fairId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile salon getir")
    public HallResponse getById(@PathVariable Long id) {
        return hallService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Yeni salon olustur", description = "fairId mevcut bir fuari gostermeli")
    public ResponseEntity<HallResponse> create(@Valid @RequestBody HallRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hallService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Salonu tamamen guncelle (PUT)")
    public HallResponse update(@PathVariable Long id, @Valid @RequestBody HallRequest request) {
        return hallService.update(id, request);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Salonu kismen guncelle (PATCH)")
    public HallResponse patch(@PathVariable Long id, @Valid @RequestBody HallPatchRequest request) {
        return hallService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Salonu sil", description = "Stanti olan salon silinemez")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hallService.delete(id);
        return ResponseEntity.noContent().build();
    }
}