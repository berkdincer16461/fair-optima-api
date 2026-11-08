package com.fairoptima.repository.impl;

import com.fairoptima.entity.Fair;
import com.fairoptima.entity.enums.FairStatus;
import com.fairoptima.repository.FairRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FairRepositoryImpl implements FairRepository {

    private final EntityManager entityManager;

    public FairRepositoryImpl(EntityManagerFactory emf) {
        this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(emf);
    }

    @Override
    public Fair save(Fair fair) {
        entityManager.persist(fair);
        return fair;
    }

    @Override
    public Optional<Fair> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Fair.class, id));
    }

    @Override
    public List<Fair> findAll() {
        return entityManager.createQuery("SELECT f FROM Fair f ORDER BY f.id", Fair.class).getResultList();
    }

    @Override
    public Fair update(Fair fair) {
        return entityManager.merge(fair);
    }

    @Override
    public void delete(Fair fair) {
        entityManager.remove(entityManager.contains(fair) ? fair : entityManager.merge(fair));
    }

    @Override
    public List<Fair> findByStatus(FairStatus status) {
        return entityManager.createQuery(
                        "SELECT f FROM Fair f WHERE f.status = :status ORDER BY f.id", Fair.class)
                .setParameter("status", status)
                .getResultList();
    }
}