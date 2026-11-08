package com.fairoptima.repository.impl;

import com.fairoptima.entity.Hall;
import com.fairoptima.repository.HallRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HallRepositoryImpl implements HallRepository {

    private final EntityManager entityManager;

    public HallRepositoryImpl(EntityManagerFactory emf) {
        this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(emf);
    }

    @Override
    public Hall save(Hall hall) {
        entityManager.persist(hall);
        return hall;
    }

    @Override
    public Optional<Hall> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Hall.class, id));
    }

    @Override
    public List<Hall> findAll() {
        return entityManager.createQuery("SELECT h FROM Hall h ORDER BY h.id", Hall.class).getResultList();
    }

    @Override
    public Hall update(Hall hall) {
        return entityManager.merge(hall);
    }

    @Override
    public void delete(Hall hall) {
        entityManager.remove(entityManager.contains(hall) ? hall : entityManager.merge(hall));
    }

    @Override
    public List<Hall> findByFairId(Long fairId) {
        return entityManager.createQuery(
                        "SELECT h FROM Hall h WHERE h.fair.id = :fairId ORDER BY h.id", Hall.class)
                .setParameter("fairId", fairId)
                .getResultList();
    }

    @Override
    public long countByFairId(Long fairId) {
        return entityManager.createQuery(
                        "SELECT COUNT(h) FROM Hall h WHERE h.fair.id = :fairId", Long.class)
                .setParameter("fairId", fairId)
                .getSingleResult();
    }

    @Override
    public long countBoothsByHallId(Long hallId) {
        return entityManager.createQuery(
                        "SELECT COUNT(b) FROM Booth b WHERE b.hall.id = :hallId", Long.class)
                .setParameter("hallId", hallId)
                .getSingleResult();
    }
}