package com.fairoptima.service.impl;

import com.fairoptima.service.PricingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service("earlyBirdPricingService")
public class EarlyBirdPricingService implements PricingService {

    private final BigDecimal discountRate;

    public EarlyBirdPricingService(@Value("${app.pricing.early-bird-discount:0.10}") BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public String getPlanName() {
        return "early";
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal basePricePerDay, int days) {
        PricingService.validate(basePricePerDay, days);
        BigDecimal total = basePricePerDay.multiply(BigDecimal.valueOf(days));
        return total.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }
}