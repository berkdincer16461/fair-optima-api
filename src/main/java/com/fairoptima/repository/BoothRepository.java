package com.fairoptima.repository;

import com.fairoptima.entity.Booth;
import java.util.List;
import java.util.Optional;

public interface BoothRepository {
    Optional<Booth> findById(Long id);
    Booth save(Booth booth);
    Booth update(Booth booth);
    void delete(Booth booth);
    List<Booth> findAll();
    List<Booth> findAvailableBoothsByFairId(Long fairId);
    Optional<Booth> findByBoothCode(String boothCode);
    long countByHallId(Long hallId);
    long countByBoothTypeId(Long boothTypeId);
}