package com.fairoptima.service;

import com.fairoptima.dto.ReservationRequest;
import com.fairoptima.dto.ReservationResponse;
import com.fairoptima.entity.*;
import com.fairoptima.entity.enums.*;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.repository.BoothRepository;
import com.fairoptima.repository.CompanyRepository;
import com.fairoptima.repository.ReservationRepository;
import com.fairoptima.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BoothRepository boothRepository;
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Booth booth;
    private Company company;
    private Fair fair;
    private Hall hall;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        fair = new Fair();
        ReflectionTestUtils.setField(fair, "id", 1L);
        fair.setStatus(FairStatus.ACTIVE);
        fair.setStartDate(LocalDate.of(2026, 12, 1));
        fair.setEndDate(LocalDate.of(2026, 12, 10));

        hall = new Hall();
        ReflectionTestUtils.setField(hall, "id", 1L);
        hall.setMaxPowerKw(100.0);
        hall.setMaxNetworkPorts(50);
        ReflectionTestUtils.setField(hall, "fair", fair);

        booth = new Booth();
        ReflectionTestUtils.setField(booth, "id", 1L);
        booth.setStatus(BoothStatus.AVAILABLE);
        ReflectionTestUtils.setField(booth, "hall", hall);
        ReflectionTestUtils.setField(booth, "boothCode", "A-101");

        company = new Company();
        ReflectionTestUtils.setField(company, "id", 1L);
        company.setName("Teknoloji A.S.");

        reservation = new Reservation();
        ReflectionTestUtils.setField(reservation, "id", 1L);
        reservation.setBooth(booth);
        reservation.setCompany(company);
        reservation.setStartDate(LocalDate.of(2026, 12, 2));
        reservation.setEndDate(LocalDate.of(2026, 12, 5));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setRequestedPowerKw(10.0);
        reservation.setRequestedNetworkPorts(5);
    }

    @Test
    @DisplayName("create: Her sey uygunsa rezervasyonu PENDING olarak kaydeder")
    void create_successful() {
        when(boothRepository.findById(1L)).thenReturn(Optional.of(booth));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(reservationRepository.save(any())).thenAnswer(i -> {
            Reservation r = i.getArgument(0);
            ReflectionTestUtils.setField(r, "id", 100L);
            return r;
        });

        ReservationRequest req = new ReservationRequest(1L, 1L, LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 5), 10.0, 5);
        ReservationResponse res = reservationService.create(req);
        
        assertEquals(100L, res.id());
        assertEquals(ReservationStatus.PENDING, res.status());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("create: Fuar aktif degilse hata firlatir")
    void create_fairNotActive_throws() {
        fair.setStatus(FairStatus.DRAFT);
        when(boothRepository.findById(1L)).thenReturn(Optional.of(booth));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        
        ReservationRequest req = new ReservationRequest(1L, 1L, LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 5), 10.0, 5);
        assertThrows(BadRequestException.class, () -> reservationService.create(req));
    }

    @Test
    @DisplayName("create: Salon elektrik kapasitesi asiliyorsa hata firlatir")
    void create_powerCapacityExceeded_throws() {
        when(boothRepository.findById(1L)).thenReturn(Optional.of(booth));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        
        // Salonda halihazirda 95 kW kullaniliyor, 10 kW daha istenirse 105 olur (100 limiti asar)
        when(reservationRepository.sumRequestedPowerByHallId(eq(1L), any(), any(), any(), any())).thenReturn(95.0);
        
        ReservationRequest req = new ReservationRequest(1L, 1L, LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 5), 10.0, 5);
        assertThrows(BadRequestException.class, () -> reservationService.create(req));
    }

    @Test
    @DisplayName("approve: Durumu APPROVED yapar ve stanti RESERVED ceker")
    void approve_successful() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.update(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));
        
        ReservationResponse res = reservationService.approve(1L);
        
        assertEquals(ReservationStatus.APPROVED, res.status());
        assertEquals(BoothStatus.RESERVED, booth.getStatus());
        verify(boothRepository).update(booth);
    }
}