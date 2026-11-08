package com.fairoptima.service;

import com.fairoptima.dto.request.FairPatchRequest;
import com.fairoptima.dto.request.FairRequest;
import com.fairoptima.dto.response.FairResponse;
import com.fairoptima.entity.enums.FairStatus;
import java.util.List;

public interface FairService {
    FairResponse create(FairRequest request);
    FairResponse getById(Long id);
    List<FairResponse> getAll(FairStatus status);
    FairResponse update(Long id, FairRequest request);
    FairResponse patch(Long id, FairPatchRequest request);
    void delete(Long id);
}