package com.fairoptima.dto;

import jakarta.validation.constraints.*;

public record CompanyPatchRequest(
        @Size(min = 1, max = 150) String name,
        @Size(min = 1, max = 100) String sector,
        @Email @Size(max = 150) String contactEmail,
        @Size(max = 255) String website,
        @Size(max = 1000) String description
) {}