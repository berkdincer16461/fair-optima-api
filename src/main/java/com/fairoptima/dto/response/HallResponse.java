package com.fairoptima.dto.response;

public record HallResponse(
        Long id,
        String name,
        Double areaSquareMeters,
        Double maxPowerKw,
        Integer maxNetworkPorts,
        Long fairId,
        String fairName
) {}