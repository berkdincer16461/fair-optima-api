package com.fairoptima.repository;

import com.fairoptima.entity.Fair;
import com.fairoptima.entity.enums.FairStatus;
import java.util.List;
import java.util.Optional;

public interface FairRepository {
    Fair save(Fair fair);
    Optional<Fair> findById(Long id);
    List<Fair> findAll();
    Fair update(Fair fair);
    void delete(Fair fair);
    List<Fair> findByStatus(FairStatus status);
}