package com.fairoptima.repository;

import com.fairoptima.entity.BoothType;
import java.util.List;
import java.util.Optional;

public interface BoothTypeRepository {
    Optional<BoothType> findById(Long id);
    BoothType save(BoothType boothType);
    BoothType update(BoothType boothType);
    void delete(BoothType boothType);
    List<BoothType> findAll();
    Optional<BoothType> findByName(String name);
}