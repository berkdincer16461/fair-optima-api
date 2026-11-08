package com.fairoptima.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HallRequest(
        @NotBlank(message = "Salon adi bos olamaz") @Size(max = 100) String name,
        @NotNull(message = "Alan buyuklugu bos olamaz") @Positive Double areaSquareMeters,
        @NotNull(message = "Azami guc bos olamaz") @Positive Double maxPowerKw,
        @NotNull(message = "Azami ag portu bos olamaz") @Positive Integer maxNetworkPorts,
        @NotNull(message = "Fuar ID bos olamaz") Long fairId
) {}