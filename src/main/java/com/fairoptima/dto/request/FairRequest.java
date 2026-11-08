package com.fairoptima.dto.request;

import com.fairoptima.entity.enums.FairStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record FairRequest(
        @NotBlank(message = "Fuar adi bos olamaz") @Size(max = 150) String name,
        @Size(max = 200) String theme,
        @NotNull(message = "Baslangic tarihi bos olamaz") LocalDate startDate,
        @NotNull(message = "Bitis tarihi bos olamaz") LocalDate endDate,
        @NotBlank(message = "Konum bos olamaz") @Size(max = 200) String location,
        FairStatus status
) {}