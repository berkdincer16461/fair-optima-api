package com.fairoptima.service.impl;

import com.fairoptima.dto.request.BoothRequest;
import com.fairoptima.dto.response.BoothResponse;
import com.fairoptima.entity.Booth;
import com.fairoptima.entity.BoothType;
import com.fairoptima.entity.Hall;
import com.fairoptima.entity.enums.BoothStatus;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.BoothRepository;
import com.fairoptima.repository.BoothTypeRepository;
import com.fairoptima.repository.HallRepository;
import com.fairoptima.repository.ReservationRepository;
import com.fairoptima.service.BoothService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoothServiceImpl implements BoothService {

    private final BoothRepository boothRepository;
    private final HallRepository hallRepository;
    private final BoothTypeRepository boothTypeRepository;
    private final ReservationRepository reservationRepository; // Adım 7: Yeni Eklendi

    // Constructor Injection güncellendi
    public BoothServiceImpl(BoothRepository boothRepository,
                            HallRepository hallRepository,
                            BoothTypeRepository boothTypeRepository,
                            ReservationRepository reservationRepository) {
        this.boothRepository = boothRepository;
        this.hallRepository = hallRepository;
        this.boothTypeRepository = boothTypeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public BoothResponse create(BoothRequest request) {
        // boothCode unique kontrolu (ayni kod varsa 400)
        if (boothRepository.findByBoothCode(request.boothCode()).isPresent()) {
            throw new BadRequestException("Bu stant kodu zaten kullanimda: " + request.boothCode());
        }

        Hall hall = hallRepository.findById(request.hallId())
                .orElseThrow(() -> new ResourceNotFoundException("Salon bulunamadi. ID: " + request.hallId()));

        BoothType boothType = boothTypeRepository.findById(request.boothTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Stant tipi bulunamadi. ID: " + request.boothTypeId()));

        Booth booth = new Booth(
                request.boothCode(),
                request.areaSquareMeters(),
                request.basePrice(),
                request.requiredPowerKw(),
                request.networkPorts(),
                BoothStatus.AVAILABLE,
                hall,
                boothType
        );

        Booth saved = boothRepository.save(booth);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BoothResponse getById(Long id) {
        Booth booth = boothRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stant bulunamadi. ID: " + id));
        return toResponse(booth);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoothResponse> getAll() {
        return boothRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoothResponse> getAvailableByFairId(Long fairId) {
        return boothRepository.findAvailableBoothsByFairId(fairId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BoothResponse update(Long id, BoothRequest request) {
        Booth existing = boothRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stant bulunamadi. ID: " + id));

        // Kod degisiyorsa baska stantta var mi kontrol et
        if (!existing.getBoothCode().equals(request.boothCode()) &&
                boothRepository.findByBoothCode(request.boothCode()).isPresent()) {
            throw new BadRequestException("Bu stant kodu zaten kullanimda: " + request.boothCode());
        }

        Hall hall = hallRepository.findById(request.hallId())
                .orElseThrow(() -> new ResourceNotFoundException("Salon bulunamadi. ID: " + request.hallId()));

        BoothType boothType = boothTypeRepository.findById(request.boothTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Stant tipi bulunamadi. ID: " + request.boothTypeId()));

        existing.setBoothCode(request.boothCode());
        existing.setAreaSquareMeters(request.areaSquareMeters());
        existing.setBasePrice(request.basePrice());
        existing.setRequiredPowerKw(request.requiredPowerKw());
        existing.setNetworkPorts(request.networkPorts());
        existing.setHall(hall);
        existing.setBoothType(boothType);

        Booth updated = boothRepository.update(existing);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public BoothResponse patch(Long id, Map<String, Object> updates) {
        Booth existing = boothRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stant bulunamadi. ID: " + id));

        try {
        updates.forEach((key, value) -> {
            if (value == null || "id".equalsIgnoreCase(key)) {
                return; // ID degistirilemez, null alanlar atlanir
            }
            switch (key) {
                case "boothCode" -> {
                    String newCode = value.toString();
                    if (!existing.getBoothCode().equals(newCode) && boothRepository.findByBoothCode(newCode).isPresent()) {
                        throw new BadRequestException("Bu stant kodu zaten kullanimda: " + newCode);
                    }
                    existing.setBoothCode(newCode);
                }
                case "areaSquareMeters" -> existing.setAreaSquareMeters(Double.valueOf(value.toString()));
                case "basePrice" -> existing.setBasePrice(new BigDecimal(value.toString()));
                case "requiredPowerKw" -> existing.setRequiredPowerKw(Double.valueOf(value.toString()));
                case "networkPorts" -> existing.setNetworkPorts(Integer.valueOf(value.toString()));
                case "status" -> existing.setStatus(BoothStatus.valueOf(value.toString()));
                case "hallId" -> {
                    Hall hall = hallRepository.findById(Long.valueOf(value.toString()))
                            .orElseThrow(() -> new ResourceNotFoundException("Salon bulunamadi: " + value));
                    existing.setHall(hall);
                }
                case "boothTypeId" -> {
                    BoothType type = boothTypeRepository.findById(Long.valueOf(value.toString()))
                            .orElseThrow(() -> new ResourceNotFoundException("Stant tipi bulunamadi: " + value));
                    existing.setBoothType(type);
                }
            }
        });
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Gecersiz alan degeri: " + e.getMessage());
        }

        Booth updated = boothRepository.update(existing);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Booth booth = boothRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stant bulunamadi. ID: " + id));
                
        // Adım 7: Stantın aktif rezervasyonu varsa silinmesini engelle
        if (reservationRepository.countByBoothId(id) > 0) {
            throw new BadRequestException("Bu stanta ait aktif rezervasyonlar bulunduğu için stant silinemez.");
        }
        
        boothRepository.delete(booth);
    }

    // Entity -> DTO Donusumu (LAZY yuklemeler serviste yapilir)
    private BoothResponse toResponse(Booth booth) {
        return new BoothResponse(
                booth.getId(),
                booth.getBoothCode(),
                booth.getAreaSquareMeters(),
                booth.getBasePrice(),
                booth.getRequiredPowerKw(),
                booth.getNetworkPorts(),
                booth.getStatus(),
                booth.getHall().getId(),
                booth.getHall().getName(),
                booth.getBoothType().getId(),
                booth.getBoothType().getName()
        );
    }
}