package com.fairoptima.repository.impl;

import com.fairoptima.entity.BoothType;
import com.fairoptima.repository.BoothTypeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BoothTypeRepositoryImpl implements BoothTypeRepository {

    private final EntityManager entityManager;

    public BoothTypeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<BoothType> findById(Long id) {
        return Optional.ofNullable(entityManager.find(BoothType.class, id));
    }
}