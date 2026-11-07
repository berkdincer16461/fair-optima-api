package com.fairoptima.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull @Positive Long boothId,
        @NotNull @Positive Long companyId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @Positive Double requestedPowerKw,
        @NotNull @PositiveOrZero Integer requestedNetworkPorts
) {}