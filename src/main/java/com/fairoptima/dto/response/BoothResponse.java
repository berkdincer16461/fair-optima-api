package com.fairoptima.dto.response;

import com.fairoptima.entity.enums.BoothStatus;
import java.math.BigDecimal;

public record BoothResponse(
        Long id,
        String boothCode,
        Double areaSquareMeters,
        BigDecimal basePrice,
        Double requiredPowerKw,
        Integer networkPorts,
        BoothStatus status,
        Long hallId,
        String hallName,
        Long boothTypeId,
        String boothTypeName
) {}