package com.fairoptima.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HallPatchRequest(
        @Size(min = 1, max = 100) String name,
        @Positive Double areaSquareMeters,
        @Positive Double maxPowerKw,
        @Positive Integer maxNetworkPorts,
        @Positive Long fairId
) {}