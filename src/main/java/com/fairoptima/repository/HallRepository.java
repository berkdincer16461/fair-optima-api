package com.fairoptima.repository;

import com.fairoptima.entity.Hall;
import java.util.Optional;

public interface HallRepository {
    Optional<Hall> findById(Long id);
}