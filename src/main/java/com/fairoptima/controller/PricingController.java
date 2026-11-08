package com.fairoptima.controller;

import com.fairoptima.dto.PriceQuoteResponse;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pricing")
@Tag(name = "Pricing", description = "Stant fiyat teklifi")
public class PricingController {

    private final PricingService standardPricing;
    private final PricingService earlyBirdPricing;

    // standardPricing: @Primary olan bean, earlyBirdPricing: @Qualifier ile secilen bean
    public PricingController(PricingService standardPricing,
                             @Qualifier("earlyBirdPricingService") PricingService earlyBirdPricing) {
        this.standardPricing = standardPricing;
        this.earlyBirdPricing = earlyBirdPricing;
    }

    @Operation(summary = "Fiyat teklifi hesapla",
            description = "plan=standard (varsayilan) veya plan=early (erken rezervasyon indirimi)")
    @GetMapping("/quote")
    public PriceQuoteResponse quote(@RequestParam(defaultValue = "1000") BigDecimal basePrice,
                                    @RequestParam(defaultValue = "1") int days,
                                    @RequestParam(defaultValue = "standard") String plan) {
        PricingService service;
        if ("standard".equalsIgnoreCase(plan)) {
            service = standardPricing;
        } else if ("early".equalsIgnoreCase(plan)) {
            service = earlyBirdPricing;
        } else {
            throw new BadRequestException("Gecersiz plan: standard veya early olmali");
        }
        return new PriceQuoteResponse(service.getPlanName(), basePrice, days,
                service.calculateTotal(basePrice, days));
    }
}