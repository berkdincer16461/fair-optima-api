package com.fairoptima.repository;

import com.fairoptima.entity.Hall;
import java.util.List;
import java.util.Optional;

public interface HallRepository {
    Hall save(Hall hall);
    Optional<Hall> findById(Long id);
    List<Hall> findAll();
    Hall update(Hall hall);
    void delete(Hall hall);
    List<Hall> findByFairId(Long fairId);
    long countByFairId(Long fairId);
    long countBoothsByHallId(Long hallId);
}