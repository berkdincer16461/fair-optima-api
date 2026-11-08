package com.fairoptima.service.impl;

import com.fairoptima.service.PricingService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Primary
public class StandardPricingService implements PricingService {

    @Override
    public String getPlanName() {
        return "standard";
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal basePricePerDay, int days) {
        PricingService.validate(basePricePerDay, days);
        return basePricePerDay.multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
    }
}