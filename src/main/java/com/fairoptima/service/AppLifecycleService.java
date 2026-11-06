package com.fairoptima.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy // Kontrol listesi: @Lazy bean gereksinimi
public class AppLifecycleService {

    private static final Logger log = LoggerFactory.getLogger(AppLifecycleService.class);

    private final String systemName;
    private final String version;
    private final String currency;

    // Kontrol listesi: @Value ile constructor injection
    public AppLifecycleService(
            @Value("${app.fair.system-name}") String systemName,
            @Value("${app.fair.version}") String version,
            @Value("${app.fair.default-currency}") String currency) {
        this.systemName = systemName;
        this.version = version;
        this.currency = currency;
    }

    @PostConstruct // Kontrol listesi: @PostConstruct metodu
    public void init() {
        log.info("[FairOptima Lifecycle] Sistem baslatildi: {} (v{}) - Para Birimi: {}", 
                systemName, version, currency);
    }

    @PreDestroy // Kontrol listesi: @PreDestroy metodu
    public void cleanup() {
        log.info("[FairOptima Lifecycle] Sistem kapatiliyor, kaynaklar serbest birakildi: {}", systemName);
    }

    public String getSystemInfo() {
        return systemName + " v" + version + " [" + currency + "]";
    }
}