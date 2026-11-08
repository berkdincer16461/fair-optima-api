package com.fairoptima.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record BoothTypePatchRequest(
        @Size(min = 1, max = 100) String name,
        @Positive @Digits(integer = 3, fraction = 2) BigDecimal priceMultiplier,
        @Positive Double defaultPowerKw,
        @Positive Integer defaultNetworkPorts
) {}