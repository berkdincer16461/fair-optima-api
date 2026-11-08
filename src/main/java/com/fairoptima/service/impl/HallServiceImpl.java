package com.fairoptima.service.impl;

import com.fairoptima.dto.request.HallPatchRequest;
import com.fairoptima.dto.request.HallRequest;
import com.fairoptima.dto.response.HallResponse;
import com.fairoptima.entity.Fair;
import com.fairoptima.entity.Hall;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.FairRepository;
import com.fairoptima.repository.HallRepository;
import com.fairoptima.service.HallService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final FairRepository fairRepository;

    public HallServiceImpl(HallRepository hallRepository, FairRepository fairRepository) {
        this.hallRepository = hallRepository;
        this.fairRepository = fairRepository;
    }

    @Override
    @Transactional
    public HallResponse create(HallRequest request) {
        Hall hall = new Hall();
        apply(hall, request);
        return toResponse(hallRepository.save(hall));
    }

    @Override
    @Transactional(readOnly = true)
    public HallResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HallResponse> getAll(Long fairId) {
        List<Hall> halls = (fairId == null) ? hallRepository.findAll() : hallRepository.findByFairId(fairId);
        return halls.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public HallResponse update(Long id, HallRequest request) {
        Hall hall = findOrThrow(id);
        apply(hall, request);
        return toResponse(hallRepository.update(hall));
    }

    @Override
    @Transactional
    public HallResponse patch(Long id, HallPatchRequest request) {
        Hall hall = findOrThrow(id);
        if (request.name() != null) hall.setName(request.name());
        if (request.areaSquareMeters() != null) hall.setAreaSquareMeters(request.areaSquareMeters());
        if (request.maxPowerKw() != null) hall.setMaxPowerKw(request.maxPowerKw());
        if (request.maxNetworkPorts() != null) hall.setMaxNetworkPorts(request.maxNetworkPorts());
        if (request.fairId() != null) hall.setFair(findFairOrThrow(request.fairId()));
        return toResponse(hallRepository.update(hall));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Hall hall = findOrThrow(id);
        if (hallRepository.countBoothsByHallId(id) > 0) {
            throw new BadRequestException("Stant bulunan salon silinemez");
        }
        hallRepository.delete(hall);
    }

    private Hall findOrThrow(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salon", id));
    }

    private Fair findFairOrThrow(Long fairId) {
        return fairRepository.findById(fairId)
                .orElseThrow(() -> new ResourceNotFoundException("Fuar", fairId));
    }

    private void apply(Hall hall, HallRequest r) {
        hall.setName(r.name());
        hall.setAreaSquareMeters(r.areaSquareMeters());
        hall.setMaxPowerKw(r.maxPowerKw());
        hall.setMaxNetworkPorts(r.maxNetworkPorts());
        hall.setFair(findFairOrThrow(r.fairId()));
    }

    private HallResponse toResponse(Hall h) {
        return new HallResponse(h.getId(), h.getName(), h.getAreaSquareMeters(), h.getMaxPowerKw(),
                h.getMaxNetworkPorts(), h.getFair().getId(), h.getFair().getName());
    }
}