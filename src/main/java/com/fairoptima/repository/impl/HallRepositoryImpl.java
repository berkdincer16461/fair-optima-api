package com.fairoptima.repository.impl;

import com.fairoptima.entity.Hall;
import com.fairoptima.repository.HallRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HallRepositoryImpl implements HallRepository {

    private final EntityManager entityManager;

    public HallRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Hall> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Hall.class, id));
    }
}