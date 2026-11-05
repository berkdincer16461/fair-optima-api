package com.fairoptima.service;

import com.fairoptima.dto.CompanyPatchRequest;
import com.fairoptima.dto.CompanyRequest;
import com.fairoptima.dto.CompanyResponse;
import java.util.List;

public interface CompanyService {
    CompanyResponse create(CompanyRequest request);
    CompanyResponse getById(Long id);
    List<CompanyResponse> getAll(String sector);
    CompanyResponse update(Long id, CompanyRequest request);
    CompanyResponse patch(Long id, CompanyPatchRequest request);
    void delete(Long id);
}