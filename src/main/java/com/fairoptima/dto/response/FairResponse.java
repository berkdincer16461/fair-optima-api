package com.fairoptima.dto.response;

import com.fairoptima.entity.enums.FairStatus;
import java.time.LocalDate;

public record FairResponse(
        Long id,
        String name,
        String theme,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        FairStatus status
) {}