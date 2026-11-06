package com.fairoptima.config;

import com.fairoptima.service.DataSeedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DataSeedService dataSeedService;

    public DataInitializer(DataSeedService dataSeedService) {
        this.dataSeedService = dataSeedService;
    }

    @Override
    public void run(String... args) {
        dataSeedService.seedIfEmpty();
    }
}