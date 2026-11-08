package com.fairoptima.service;

import com.fairoptima.dto.request.FairPatchRequest;
import com.fairoptima.dto.request.FairRequest;
import com.fairoptima.dto.response.FairResponse;
import com.fairoptima.entity.Fair;
import com.fairoptima.entity.enums.FairStatus;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.repository.FairRepository;
import com.fairoptima.repository.HallRepository;
import com.fairoptima.service.impl.FairServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FairServiceTest {

    @Mock
    private FairRepository fairRepository;

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private FairServiceImpl fairService;

    private Fair existing() {
        Fair f = new Fair("Fuar", "Tema", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Trabzon", FairStatus.DRAFT);
        ReflectionTestUtils.setField(f, "id", 1L);
        return f;
    }

    @Test
    void create_whenEndBeforeStart_throwsBadRequest() {
        FairRequest request = new FairRequest("A", "T", LocalDate.of(2026, 12, 5),
                LocalDate.of(2026, 12, 1), "Trabzon", null);

        assertThrows(BadRequestException.class, () -> fairService.create(request));

        verify(fairRepository, never()).save(any(Fair.class));
    }

    @Test
    void delete_whenFairHasHalls_throwsBadRequest() {
        when(fairRepository.findById(1L)).thenReturn(Optional.of(existing()));
        when(hallRepository.countByFairId(1L)).thenReturn(2L);

        assertThrows(BadRequestException.class, () -> fairService.delete(1L));

        verify(fairRepository, never()).delete(any(Fair.class));
    }

    @Test
    void patch_changesOnlySentFields() {
        when(fairRepository.findById(1L)).thenReturn(Optional.of(existing()));
        when(fairRepository.update(any(Fair.class))).thenAnswer(inv -> inv.getArgument(0));

        FairResponse r = fairService.patch(1L,
                new FairPatchRequest(null, null, null, null, null, FairStatus.ACTIVE));

        assertEquals(FairStatus.ACTIVE, r.status());
        assertEquals("Fuar", r.name());
        assertEquals("Trabzon", r.location());
    }
}