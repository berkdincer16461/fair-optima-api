package com.fairoptima.dto;

import jakarta.validation.constraints.*;

public record CompanyRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 100) String sector,
        @NotBlank @Email @Size(max = 150) String contactEmail,
        @Size(max = 255) String website,
        @Size(max = 1000) String description
) {}