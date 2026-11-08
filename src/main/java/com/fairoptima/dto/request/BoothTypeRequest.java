package com.fairoptima.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record BoothTypeRequest(
        @NotBlank(message = "Stant tipi adi bos olamaz") @Size(max = 100) String name,
        @NotNull(message = "Fiyat carpani bos olamaz") @Positive @Digits(integer = 3, fraction = 2) BigDecimal priceMultiplier,
        @NotNull(message = "Varsayilan guc bos olamaz") @Positive Double defaultPowerKw,
        @NotNull(message = "Varsayilan ag portu bos olamaz") @Positive Integer defaultNetworkPorts
) {}