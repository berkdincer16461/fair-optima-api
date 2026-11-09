package com.fairoptima.service;

import com.fairoptima.dto.ReservationResponse;
import com.fairoptima.entity.Booth;
import com.fairoptima.entity.Company;
import com.fairoptima.entity.Reservation;
import com.fairoptima.entity.enums.BoothStatus;
import com.fairoptima.entity.enums.ReservationStatus;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.repository.BoothRepository;
import com.fairoptima.repository.CompanyRepository;
import com.fairoptima.repository.ReservationRepository;
import com.fairoptima.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationStatusTransitionTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BoothRepository boothRepository;
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Booth booth;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        booth = new Booth();
        ReflectionTestUtils.setField(booth, "id", 1L);
        ReflectionTestUtils.setField(booth, "boothCode", "A-101");
        booth.setStatus(BoothStatus.AVAILABLE);

        Company company = new Company();
        ReflectionTestUtils.setField(company, "id", 1L);
        company.setName("Teknoloji A.S.");

        reservation = new Reservation();
        ReflectionTestUtils.setField(reservation, "id", 1L);
        reservation.setBooth(booth);
        reservation.setCompany(company);
        reservation.setStatus(ReservationStatus.PENDING);
    }

    @Test
    void reject_pending_setsRejected() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationResponse r = reservationService.reject(1L);

        assertEquals(ReservationStatus.REJECTED, r.status());
        verify(reservationRepository).update(reservation);
    }

    @Test
    void reject_whenNotPending_throws() {
        reservation.setStatus(ReservationStatus.APPROVED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(BadRequestException.class, () -> reservationService.reject(1L));

        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void cancel_approved_freesBooth() {
        reservation.setStatus(ReservationStatus.APPROVED);
        booth.setStatus(BoothStatus.RESERVED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationResponse r = reservationService.cancel(1L);

        assertEquals(ReservationStatus.CANCELLED, r.status());
        assertEquals(BoothStatus.AVAILABLE, booth.getStatus());
        verify(boothRepository).update(booth);
    }

    @Test
    void cancel_whenAlreadyCancelled_throws() {
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(BadRequestException.class, () -> reservationService.cancel(1L));

        verify(reservationRepository, never()).update(any(Reservation.class));
    }
}