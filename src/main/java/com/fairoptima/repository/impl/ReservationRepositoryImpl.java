package com.fairoptima.repository.impl;

import com.fairoptima.entity.Reservation;
import com.fairoptima.entity.enums.ReservationStatus;
import com.fairoptima.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final EntityManager entityManager;

    public ReservationRepositoryImpl(EntityManagerFactory emf) {
        this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(emf);
    }

    @Override
    public Reservation save(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Reservation.class, id));
    }

    @Override
    public List<Reservation> findAll() {
        return entityManager.createQuery("SELECT r FROM Reservation r ORDER BY r.createdAt DESC", Reservation.class)
                .getResultList();
    }

    @Override
    public Reservation update(Reservation reservation) {
        return entityManager.merge(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        entityManager.remove(entityManager.contains(reservation) ? reservation : entityManager.merge(reservation));
    }

    @Override
    public long countConflictingReservations(Long boothId, List<ReservationStatus> statuses, LocalDate start, LocalDate end, Long excludeId) {
        String jpql = "SELECT COUNT(r) FROM Reservation r WHERE r.booth.id = :boothId AND r.status IN :statuses " +
                      "AND r.startDate <= :end AND r.endDate >= :start AND (:excludeId IS NULL OR r.id <> :excludeId)";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("boothId", boothId)
                .setParameter("statuses", statuses)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("excludeId", excludeId)
                .getSingleResult();
    }

    @Override
    public Double sumRequestedPowerByHallId(Long hallId, List<ReservationStatus> statuses, LocalDate start, LocalDate end, Long excludeId) {
        String jpql = "SELECT SUM(r.requestedPowerKw) FROM Reservation r WHERE r.booth.hall.id = :hallId AND r.status IN :statuses " +
                      "AND r.startDate <= :end AND r.endDate >= :start AND (:excludeId IS NULL OR r.id <> :excludeId)";
        Double sum = entityManager.createQuery(jpql, Double.class)
                .setParameter("hallId", hallId)
                .setParameter("statuses", statuses)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("excludeId", excludeId)
                .getSingleResult();
        return sum != null ? sum : 0.0;
    }

    @Override
    public Integer sumRequestedNetworkPortsByHallId(Long hallId, List<ReservationStatus> statuses, LocalDate start, LocalDate end, Long excludeId) {
        String jpql = "SELECT SUM(r.requestedNetworkPorts) FROM Reservation r WHERE r.booth.hall.id = :hallId AND r.status IN :statuses " +
                      "AND r.startDate <= :end AND r.endDate >= :start AND (:excludeId IS NULL OR r.id <> :excludeId)";
        Long sum = entityManager.createQuery(jpql, Long.class)
                .setParameter("hallId", hallId)
                .setParameter("statuses", statuses)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("excludeId", excludeId)
                .getSingleResult();
        return sum != null ? sum.intValue() : 0;
    }

    @Override
    public long countByCompanyId(Long companyId) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.company.id = :companyId", Long.class)
                .setParameter("companyId", companyId)
                .getSingleResult();
    }

    @Override
    public long countByBoothId(Long boothId) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.booth.id = :boothId", Long.class)
                .setParameter("boothId", boothId)
                .getSingleResult();
    }

    @Override
    public List<Reservation> findByCompanyId(Long companyId) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.company.id = :companyId ORDER BY r.startDate DESC", Reservation.class)
                .setParameter("companyId", companyId)
                .getResultList();
    }
}