package com.fairoptima.dto;

import java.math.BigDecimal;

public record PriceQuoteResponse(
        String plan,
        BigDecimal basePricePerDay,
        int days,
        BigDecimal total
) {}