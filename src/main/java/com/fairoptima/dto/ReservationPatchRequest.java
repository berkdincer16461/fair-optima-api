package com.fairoptima.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record ReservationPatchRequest(
        LocalDate startDate,
        LocalDate endDate,
        @Positive Double requestedPowerKw,
        @PositiveOrZero Integer requestedNetworkPorts
) {}