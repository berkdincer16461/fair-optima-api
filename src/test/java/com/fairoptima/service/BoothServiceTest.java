package com.fairoptima.service;

import com.fairoptima.dto.request.BoothRequest;
import com.fairoptima.dto.response.BoothResponse;
import com.fairoptima.entity.Booth;
import com.fairoptima.entity.BoothType;
import com.fairoptima.entity.Hall;
import com.fairoptima.entity.enums.BoothStatus;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.BoothRepository;
import com.fairoptima.repository.BoothTypeRepository;
import com.fairoptima.repository.HallRepository;
import com.fairoptima.repository.ReservationRepository;
import com.fairoptima.service.impl.BoothServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoothServiceTest {

    @Mock
    private BoothRepository boothRepository;
    @Mock
    private HallRepository hallRepository;
    @Mock
    private BoothTypeRepository boothTypeRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private BoothServiceImpl boothService;

    private Booth sampleBooth;
    private Hall sampleHall;
    private BoothType sampleType;
    private BoothRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleHall = new Hall();
        ReflectionTestUtils.setField(sampleHall, "id", 1L);
        sampleHall.setName("Ana Salon");

        sampleType = new BoothType();
        ReflectionTestUtils.setField(sampleType, "id", 1L);
        sampleType.setName("Standart Stant");

        sampleBooth = new Booth("A1", 10.0, new BigDecimal("1000"), 5.0, 2, BoothStatus.AVAILABLE, sampleHall, sampleType);
        ReflectionTestUtils.setField(sampleBooth, "id", 100L);

        sampleRequest = new BoothRequest("A1", 10.0, new BigDecimal("1000"), 5.0, 2, 1L, 1L);
    }

    @Test
    @DisplayName("Başarılı stant oluşturma testi")
    void shouldCreateBoothSuccessfully() {
        when(boothRepository.findByBoothCode("A1")).thenReturn(Optional.empty());
        when(hallRepository.findById(1L)).thenReturn(Optional.of(sampleHall));
        when(boothTypeRepository.findById(1L)).thenReturn(Optional.of(sampleType));
        when(boothRepository.save(any(Booth.class))).thenReturn(sampleBooth);

        BoothResponse response = boothService.create(sampleRequest);

        assertNotNull(response);
        assertEquals("A1", response.boothCode());
        verify(boothRepository).save(any(Booth.class));
    }

    @Test
    @DisplayName("Aynı kodla stant oluşturmaya çalışınca hata fırlatma testi")
    void shouldThrowExceptionWhenCreateWithDuplicateCode() {
        when(boothRepository.findByBoothCode("A1")).thenReturn(Optional.of(sampleBooth));

        assertThrows(BadRequestException.class, () -> boothService.create(sampleRequest));
        verify(boothRepository, never()).save(any());
    }

    @Test
    @DisplayName("ID ile başarılı stant getirme testi")
    void shouldGetBoothById() {
        when(boothRepository.findById(100L)).thenReturn(Optional.of(sampleBooth));

        BoothResponse response = boothService.getById(100L);

        assertNotNull(response);
        assertEquals(100L, response.id());
    }

    @Test
    @DisplayName("Rezervasyonu olmayan stantı başarıyla silme testi")
    void shouldDeleteBoothSuccessfully() {
        when(boothRepository.findById(100L)).thenReturn(Optional.of(sampleBooth));
        when(reservationRepository.countByBoothId(100L)).thenReturn(0L); // 0 rezervasyon

        assertDoesNotThrow(() -> boothService.delete(100L));
        verify(boothRepository).delete(sampleBooth);
    }

    @Test
    @DisplayName("Aktif rezervasyonu olan stantı silerken kurala takılma (Adım 7) testi")
    void shouldThrowExceptionWhenDeleteBoothWithActiveReservations() {
        when(boothRepository.findById(100L)).thenReturn(Optional.of(sampleBooth));
        when(reservationRepository.countByBoothId(100L)).thenReturn(1L); // 1 aktif rezervasyon var

        BadRequestException ex = assertThrows(BadRequestException.class, () -> boothService.delete(100L));
        assertTrue(ex.getMessage().contains("rezervasyonlar bulunduğu için stant silinemez"));
        verify(boothRepository, never()).delete(any()); // Repository'nin delete metodu HİÇ çağrılmamalı
    }
}