package com.fairoptima.repository.impl;

import com.fairoptima.entity.BoothType;
import com.fairoptima.repository.BoothTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoothTypeRepositoryImpl implements BoothTypeRepository {

    private final EntityManager entityManager;

    public BoothTypeRepositoryImpl(EntityManagerFactory emf) {
        this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(emf);
    }

    @Override
    public BoothType save(BoothType boothType) {
        entityManager.persist(boothType);
        return boothType;
    }

    @Override
    public Optional<BoothType> findById(Long id) {
        return Optional.ofNullable(entityManager.find(BoothType.class, id));
    }

    @Override
    public List<BoothType> findAll() {
        return entityManager.createQuery("SELECT t FROM BoothType t ORDER BY t.id", BoothType.class).getResultList();
    }

    @Override
    public BoothType update(BoothType boothType) {
        return entityManager.merge(boothType);
    }

    @Override
    public void delete(BoothType boothType) {
        entityManager.remove(entityManager.contains(boothType) ? boothType : entityManager.merge(boothType));
    }

    @Override
    public Optional<BoothType> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT t FROM BoothType t WHERE t.name = :name", BoothType.class)
                .setParameter("name", name)
                .getResultStream().findFirst();
    }

    @Override
    public long countBoothsByTypeId(Long boothTypeId) {
        return entityManager.createQuery(
                        "SELECT COUNT(b) FROM Booth b WHERE b.boothType.id = :typeId", Long.class)
                .setParameter("typeId", boothTypeId)
                .getSingleResult();
    }
}