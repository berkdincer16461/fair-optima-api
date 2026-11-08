package com.fairoptima.repository;

import com.fairoptima.entity.BoothType;
import java.util.List;
import java.util.Optional;

public interface BoothTypeRepository {
    BoothType save(BoothType boothType);
    Optional<BoothType> findById(Long id);
    List<BoothType> findAll();
    BoothType update(BoothType boothType);
    void delete(BoothType boothType);
    Optional<BoothType> findByName(String name);
    long countBoothsByTypeId(Long boothTypeId);
}