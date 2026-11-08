package com.fairoptima.service;

import com.fairoptima.dto.request.HallPatchRequest;
import com.fairoptima.dto.request.HallRequest;
import com.fairoptima.dto.response.HallResponse;
import java.util.List;

public interface HallService {
    HallResponse create(HallRequest request);
    HallResponse getById(Long id);
    List<HallResponse> getAll(Long fairId);
    HallResponse update(Long id, HallRequest request);
    HallResponse patch(Long id, HallPatchRequest request);
    void delete(Long id);
}