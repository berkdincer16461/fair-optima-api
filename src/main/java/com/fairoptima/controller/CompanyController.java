package com.fairoptima.controller;

import com.fairoptima.dto.CompanyPatchRequest;
import com.fairoptima.dto.CompanyRequest;
import com.fairoptima.dto.CompanyResponse;
import com.fairoptima.exception.ErrorResponse;
import com.fairoptima.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "Katilimci sirket yonetimi")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "Sirketleri listele",
            description = "Tum sirketleri dondurur. Istege bagli sector parametresi ile filtrelenir. Rol: READONLY, COMPANY, ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste dondu"),
            @ApiResponse(responseCode = "401", description = "Kimlik dogrulama basarisiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<CompanyResponse> getAll(@RequestParam(required = false) String sector) {
        return companyService.getAll(sector);
    }

    @Operation(summary = "Kimlige gore sirket getir", description = "Rol: READONLY, COMPANY, ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sirket bulundu"),
            @ApiResponse(responseCode = "401", description = "Kimlik dogrulama basarisiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sirket bulunamadi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public CompanyResponse getById(@PathVariable Long id) {
        return companyService.getById(id);
    }

    @Operation(summary = "Yeni sirket olustur",
            description = "Govdedeki id yok sayilir, kimligi sistem uretir. Rol: COMPANY, ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sirket olusturuldu"),
            @ApiResponse(responseCode = "400", description = "Gecersiz istek govdesi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Kimlik dogrulama basarisiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Yetki yetersiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
    }

    @Operation(summary = "Sirketi tamamen guncelle (PUT)",
            description = "Tum alanlar degisir. Rol: COMPANY, ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Guncellendi"),
            @ApiResponse(responseCode = "400", description = "Gecersiz istek govdesi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Kimlik dogrulama basarisiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Yetki yetersiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sirket bulunamadi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return companyService.update(id, request);
    }

    @Operation(summary = "Sirketi kismen guncelle (PATCH)",
            description = "Yalnizca gonderilen alanlar degisir, id degistirilemez. Rol: COMPANY, ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kismen guncellendi"),
            @ApiResponse(responseCode = "400", description = "Gecersiz istek govdesi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Kimlik dogrulama basarisiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Yetki yetersiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sirket bulunamadi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public CompanyResponse patch(@PathVariable Long id, @Valid @RequestBody CompanyPatchRequest request) {
        return companyService.patch(id, request);
    }

    @Operation(summary = "Sirketi sil", description = "Rol: yalnizca ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Silindi"),
            @ApiResponse(responseCode = "401", description = "Kimlik dogrulama basarisiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Yetki yetersiz",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sirket bulunamadi",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}