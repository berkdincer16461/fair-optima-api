package com.fairoptima.repository;

import com.fairoptima.entity.BoothType;
import java.util.Optional;

public interface BoothTypeRepository {
    Optional<BoothType> findById(Long id);
}