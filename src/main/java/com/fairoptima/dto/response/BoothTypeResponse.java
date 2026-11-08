package com.fairoptima.dto.response;

import java.math.BigDecimal;

public record BoothTypeResponse(
        Long id,
        String name,
        BigDecimal priceMultiplier,
        Double defaultPowerKw,
        Integer defaultNetworkPorts
) {}