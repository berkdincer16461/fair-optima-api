package com.fairoptima.dto;

import com.fairoptima.entity.enums.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long boothId,
        String boothCode,
        Long companyId,
        String companyName,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status,
        Double requestedPowerKw,
        Integer requestedNetworkPorts,
        LocalDateTime createdAt
) {}