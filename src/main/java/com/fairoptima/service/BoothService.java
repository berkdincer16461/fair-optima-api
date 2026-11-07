package com.fairoptima.service;

import com.fairoptima.dto.request.BoothRequest;
import com.fairoptima.dto.response.BoothResponse;
import java.util.List;
import java.util.Map;

public interface BoothService {
    BoothResponse create(BoothRequest request);
    BoothResponse getById(Long id);
    List<BoothResponse> getAll();
    List<BoothResponse> getAvailableByFairId(Long fairId);
    BoothResponse update(Long id, BoothRequest request);
    BoothResponse patch(Long id, Map<String, Object> updates);
    void delete(Long id);
}