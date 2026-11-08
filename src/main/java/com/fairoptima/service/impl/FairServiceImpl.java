package com.fairoptima.service.impl;

import com.fairoptima.dto.request.FairPatchRequest;
import com.fairoptima.dto.request.FairRequest;
import com.fairoptima.dto.response.FairResponse;
import com.fairoptima.entity.Fair;
import com.fairoptima.entity.enums.FairStatus;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.FairRepository;
import com.fairoptima.repository.HallRepository;
import com.fairoptima.service.FairService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FairServiceImpl implements FairService {

    private final FairRepository fairRepository;
    private final HallRepository hallRepository;

    public FairServiceImpl(FairRepository fairRepository, HallRepository hallRepository) {
        this.fairRepository = fairRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    @Transactional
    public FairResponse create(FairRequest request) {
        validateDates(request.startDate(), request.endDate());
        Fair fair = new Fair();
        apply(fair, request);
        fair.setStatus(request.status() != null ? request.status() : FairStatus.DRAFT);
        return toResponse(fairRepository.save(fair));
    }

    @Override
    @Transactional(readOnly = true)
    public FairResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FairResponse> getAll(FairStatus status) {
        List<Fair> fairs = (status == null) ? fairRepository.findAll() : fairRepository.findByStatus(status);
        return fairs.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public FairResponse update(Long id, FairRequest request) {
        Fair fair = findOrThrow(id);
        validateDates(request.startDate(), request.endDate());
        apply(fair, request);
        if (request.status() != null) {
            fair.setStatus(request.status());
        }
        return toResponse(fairRepository.update(fair));
    }

    @Override
    @Transactional
    public FairResponse patch(Long id, FairPatchRequest request) {
        Fair fair = findOrThrow(id);
        if (request.name() != null) fair.setName(request.name());
        if (request.theme() != null) fair.setTheme(request.theme());
        if (request.startDate() != null) fair.setStartDate(request.startDate());
        if (request.endDate() != null) fair.setEndDate(request.endDate());
        if (request.location() != null) fair.setLocation(request.location());
        if (request.status() != null) fair.setStatus(request.status());
        validateDates(fair.getStartDate(), fair.getEndDate());
        return toResponse(fairRepository.update(fair));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Fair fair = findOrThrow(id);
        if (hallRepository.countByFairId(id) > 0) {
            throw new BadRequestException("Salonu bulunan fuar silinemez");
        }
        fairRepository.delete(fair);
    }

    private Fair findOrThrow(Long id) {
        return fairRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fuar", id));
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Bitis tarihi baslangic tarihinden once olamaz");
        }
    }

    private void apply(Fair fair, FairRequest r) {
        fair.setName(r.name());
        fair.setTheme(r.theme());
        fair.setStartDate(r.startDate());
        fair.setEndDate(r.endDate());
        fair.setLocation(r.location());
    }

    private FairResponse toResponse(Fair f) {
        return new FairResponse(f.getId(), f.getName(), f.getTheme(), f.getStartDate(),
                f.getEndDate(), f.getLocation(), f.getStatus());
    }
}