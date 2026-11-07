package com.fairoptima.repository.impl;

import com.fairoptima.entity.Booth;
import com.fairoptima.entity.enums.BoothStatus;
import com.fairoptima.repository.BoothRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoothRepositoryImpl implements BoothRepository {

    private final EntityManager entityManager;

    public BoothRepositoryImpl(EntityManagerFactory emf) {
        this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(emf);
    }

    @Override
    public Booth save(Booth booth) {
        entityManager.persist(booth);
        return booth;
    }

    @Override
    public Optional<Booth> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Booth.class, id));
    }

    @Override
    public List<Booth> findAll() {
        return entityManager.createQuery("SELECT b FROM Booth b ORDER BY b.id", Booth.class)
                .getResultList();
    }

    @Override
    public Booth update(Booth booth) {
        return entityManager.merge(booth);
    }

    @Override
    public void delete(Booth booth) {
        entityManager.remove(entityManager.contains(booth) ? booth : entityManager.merge(booth));
    }

    @Override
    public List<Booth> findAvailableBoothsByFairId(Long fairId) {
        return entityManager.createQuery(
                        "SELECT b FROM Booth b WHERE b.hall.fair.id = :fairId AND b.status = :status ORDER BY b.boothCode",
                        Booth.class)
                .setParameter("fairId", fairId)
                .setParameter("status", BoothStatus.AVAILABLE)
                .getResultList();
    }

    @Override
    public Optional<Booth> findByBoothCode(String boothCode) {
        return entityManager.createQuery("SELECT b FROM Booth b WHERE b.boothCode = :code", Booth.class)
                .setParameter("code", boothCode)
                .getResultStream()
                .findFirst();
    }

    @Override
    public long countByHallId(Long hallId) {
        return entityManager.createQuery("SELECT COUNT(b) FROM Booth b WHERE b.hall.id = :hallId", Long.class)
                .setParameter("hallId", hallId)
                .getSingleResult();
    }

    @Override
    public long countByBoothTypeId(Long boothTypeId) {
        return entityManager.createQuery("SELECT COUNT(b) FROM Booth b WHERE b.boothType.id = :typeId", Long.class)
                .setParameter("typeId", boothTypeId)
                .getSingleResult();
    }
}