package com.fairoptima.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BoothRequest(
        @NotBlank(message = "Stant kodu bos olamaz")
        String boothCode,

        @NotNull(message = "Alan buyuklugu bos olamaz")
        @Positive(message = "Alan buyuklugu pozitif olmalidir")
        Double areaSquareMeters,

        @NotNull(message = "Baz fiyat bos olamaz")
        @Positive(message = "Baz fiyat pozitif olmalidir")
        BigDecimal basePrice,

        @NotNull(message = "Gerekli guc bos olamaz")
        @Positive(message = "Gerekli guc pozitif olmalidir")
        Double requiredPowerKw,

        @NotNull(message = "Ag portu bos olamaz")
        @Positive(message = "Ag port sayisi pozitif olmalidir")
        Integer networkPorts,

        @NotNull(message = "Salon ID bos olamaz")
        Long hallId,

        @NotNull(message = "Stant tipi ID bos olamaz")
        Long boothTypeId
) {}