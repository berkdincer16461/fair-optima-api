package com.fairoptima.service;

import com.fairoptima.exception.BadRequestException;
import com.fairoptima.service.impl.EarlyBirdPricingService;
import com.fairoptima.service.impl.StandardPricingService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PricingServiceTest {

    @Test
    void standard_multipliesByDays() {
        assertEquals(new BigDecimal("3000.00"),
                new StandardPricingService().calculateTotal(new BigDecimal("1000"), 3));
    }

    @Test
    void earlyBird_appliesDiscount() {
        assertEquals(new BigDecimal("2700.00"),
                new EarlyBirdPricingService(new BigDecimal("0.10")).calculateTotal(new BigDecimal("1000"), 3));
    }

    @Test
    void invalidDays_throwsBadRequest() {
        assertThrows(BadRequestException.class,
                () -> new StandardPricingService().calculateTotal(BigDecimal.TEN, 0));
    }
}