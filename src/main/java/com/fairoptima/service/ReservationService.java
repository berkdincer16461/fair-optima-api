package com.fairoptima.service;

import com.fairoptima.dto.ReservationPatchRequest;
import com.fairoptima.dto.ReservationRequest;
import com.fairoptima.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {
    List<ReservationResponse> getAll();
    ReservationResponse getById(Long id);
    ReservationResponse create(ReservationRequest request);
    ReservationResponse update(Long id, ReservationRequest request);
    ReservationResponse patch(Long id, ReservationPatchRequest request);
    void delete(Long id);

    ReservationResponse approve(Long id);
    ReservationResponse reject(Long id);
    ReservationResponse cancel(Long id);
}