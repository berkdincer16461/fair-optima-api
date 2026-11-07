package com.fairoptima.repository;

import com.fairoptima.entity.Hall;
import java.util.List;
import java.util.Optional;

public interface HallRepository {
    Optional<Hall> findById(Long id);
    Hall save(Hall hall);
    Hall update(Hall hall);
    void delete(Hall hall);
    List<Hall> findAll();
    List<Hall> findByFairId(Long fairId);
    long countByFairId(Long fairId);
}