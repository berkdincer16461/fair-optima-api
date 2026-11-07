package com.fairoptima.repository;

import com.fairoptima.entity.Reservation;
import com.fairoptima.entity.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    Reservation update(Reservation reservation);
    void delete(Reservation reservation);
    
    long countConflictingReservations(Long boothId, List<ReservationStatus> statuses, LocalDate start, LocalDate end, Long excludeId);
    Double sumRequestedPowerByHallId(Long hallId, List<ReservationStatus> statuses, LocalDate start, LocalDate end, Long excludeId);
    Integer sumRequestedNetworkPortsByHallId(Long hallId, List<ReservationStatus> statuses, LocalDate start, LocalDate end, Long excludeId);
    
    long countByCompanyId(Long companyId);
    long countByBoothId(Long boothId);
    List<Reservation> findByCompanyId(Long companyId);
}