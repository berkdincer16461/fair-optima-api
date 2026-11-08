package com.fairoptima.service.impl;

import com.fairoptima.dto.request.BoothTypePatchRequest;
import com.fairoptima.dto.request.BoothTypeRequest;
import com.fairoptima.dto.response.BoothTypeResponse;
import com.fairoptima.entity.BoothType;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.BoothTypeRepository;
import com.fairoptima.service.BoothTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoothTypeServiceImpl implements BoothTypeService {

    private final BoothTypeRepository boothTypeRepository;

    public BoothTypeServiceImpl(BoothTypeRepository boothTypeRepository) {
        this.boothTypeRepository = boothTypeRepository;
    }

    @Override
    @Transactional
    public BoothTypeResponse create(BoothTypeRequest request) {
        checkNameUnique(request.name(), null);
        BoothType type = new BoothType();
        apply(type, request);
        return toResponse(boothTypeRepository.save(type));
    }

    @Override
    @Transactional(readOnly = true)
    public BoothTypeResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoothTypeResponse> getAll() {
        return boothTypeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public BoothTypeResponse update(Long id, BoothTypeRequest request) {
        BoothType type = findOrThrow(id);
        checkNameUnique(request.name(), id);
        apply(type, request);
        return toResponse(boothTypeRepository.update(type));
    }

    @Override
    @Transactional
    public BoothTypeResponse patch(Long id, BoothTypePatchRequest request) {
        BoothType type = findOrThrow(id);
        if (request.name() != null) {
            checkNameUnique(request.name(), id);
            type.setName(request.name());
        }
        if (request.priceMultiplier() != null) type.setPriceMultiplier(request.priceMultiplier());
        if (request.defaultPowerKw() != null) type.setDefaultPowerKw(request.defaultPowerKw());
        if (request.defaultNetworkPorts() != null) type.setDefaultNetworkPorts(request.defaultNetworkPorts());
        return toResponse(boothTypeRepository.update(type));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BoothType type = findOrThrow(id);
        if (boothTypeRepository.countBoothsByTypeId(id) > 0) {
            throw new BadRequestException("Stant bulunan stant tipi silinemez");
        }
        boothTypeRepository.delete(type);
    }

    private BoothType findOrThrow(Long id) {
        return boothTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stant tipi", id));
    }

    private void checkNameUnique(String name, Long selfId) {
        boothTypeRepository.findByName(name).ifPresent(existing -> {
            if (selfId == null || !existing.getId().equals(selfId)) {
                throw new BadRequestException("Bu stant tipi adi zaten kullanimda: " + name);
            }
        });
    }

    private void apply(BoothType t, BoothTypeRequest r) {
        t.setName(r.name());
        t.setPriceMultiplier(r.priceMultiplier());
        t.setDefaultPowerKw(r.defaultPowerKw());
        t.setDefaultNetworkPorts(r.defaultNetworkPorts());
    }

    private BoothTypeResponse toResponse(BoothType t) {
        return new BoothTypeResponse(t.getId(), t.getName(), t.getPriceMultiplier(),
                t.getDefaultPowerKw(), t.getDefaultNetworkPorts());
    }
}