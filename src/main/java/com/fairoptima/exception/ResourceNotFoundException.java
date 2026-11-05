package com.fairoptima.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " bulunamadı (ID: " + id + ")");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}