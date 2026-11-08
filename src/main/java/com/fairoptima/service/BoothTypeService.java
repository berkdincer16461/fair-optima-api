package com.fairoptima.service;

import com.fairoptima.dto.request.BoothTypePatchRequest;
import com.fairoptima.dto.request.BoothTypeRequest;
import com.fairoptima.dto.response.BoothTypeResponse;
import java.util.List;

public interface BoothTypeService {
    BoothTypeResponse create(BoothTypeRequest request);
    BoothTypeResponse getById(Long id);
    List<BoothTypeResponse> getAll();
    BoothTypeResponse update(Long id, BoothTypeRequest request);
    BoothTypeResponse patch(Long id, BoothTypePatchRequest request);
    void delete(Long id);
}