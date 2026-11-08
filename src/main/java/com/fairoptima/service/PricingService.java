package com.fairoptima.service;

import com.fairoptima.exception.BadRequestException;
import java.math.BigDecimal;

public interface PricingService {

    String getPlanName();

    BigDecimal calculateTotal(BigDecimal basePricePerDay, int days);

    static void validate(BigDecimal basePricePerDay, int days) {
        if (basePricePerDay == null || basePricePerDay.signum() <= 0) {
            throw new BadRequestException("Gunluk fiyat sifirdan buyuk olmali");
        }
        if (days < 1) {
            throw new BadRequestException("Gun sayisi en az 1 olmali");
        }
    }
}