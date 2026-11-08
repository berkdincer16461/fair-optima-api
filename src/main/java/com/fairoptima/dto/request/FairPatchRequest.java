package com.fairoptima.dto.request;

import com.fairoptima.entity.enums.FairStatus;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record FairPatchRequest(
        @Size(min = 1, max = 150) String name,
        @Size(max = 200) String theme,
        LocalDate startDate,
        LocalDate endDate,
        @Size(min = 1, max = 200) String location,
        FairStatus status
) {}