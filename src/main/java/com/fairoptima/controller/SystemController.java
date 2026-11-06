package com.fairoptima.controller;

import com.fairoptima.service.AppLifecycleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    private final AppLifecycleService appLifecycleService;

    // @Lazy enjeksiyonu: Bean sadece bu uca ilk istek geldiginde bellege yuklenir
    public SystemController(@Lazy AppLifecycleService appLifecycleService) {
        this.appLifecycleService = appLifecycleService;
    }

    @GetMapping("/info")
public ResponseEntity<Map<String, Object>> getSystemInfo() {
    return ResponseEntity.ok(Map.of("systemInfo", appLifecycleService.getSystemInfo()));
}
}