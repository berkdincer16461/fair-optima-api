package com.fairoptima.controller;

import com.fairoptima.dto.ReservationPatchRequest;
import com.fairoptima.dto.ReservationRequest;
import com.fairoptima.dto.ReservationResponse;
import com.fairoptima.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Rezervasyon Yönetimi API'si")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary = "Tüm rezervasyonları listele")
    public ResponseEntity<List<ReservationResponse>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile rezervasyon getir")
    public ResponseEntity<ReservationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Yeni rezervasyon oluştur (PENDING durumuyla başlar)")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Rezervasyonu tamamen güncelle (Sadece PENDING durumunda)")
    public ResponseEntity<ReservationResponse> update(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Rezervasyonu kısmen güncelle (Patch - Sadece PENDING durumunda)")
    public ResponseEntity<ReservationResponse> patch(@PathVariable Long id, @Valid @RequestBody ReservationPatchRequest request) {
        return ResponseEntity.ok(reservationService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Rezervasyonu sil (Onaylanmış rezervasyonlar silinemez)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Rezervasyonu onayla ve stantı rezerve et (Sadece ADMIN)")
    public ResponseEntity<ReservationResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.approve(id));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Rezervasyonu reddet ve stantı boşa çıkar (Sadece ADMIN)")
    public ResponseEntity<ReservationResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.reject(id));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Rezervasyonu iptal et ve stantı boşa çıkar")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancel(id));
    }
}