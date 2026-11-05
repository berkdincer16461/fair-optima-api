package com.fairoptima.dto;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String sector,
        String contactEmail,
        String website,
        String description,
        LocalDateTime createdAt
) {}