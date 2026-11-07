package com.fairoptima.service;

import com.fairoptima.dto.CompanyPatchRequest;
import com.fairoptima.dto.CompanyRequest;
import com.fairoptima.dto.CompanyResponse;
import com.fairoptima.entity.Company;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.CompanyRepository;
import com.fairoptima.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import com.fairoptima.repository.ReservationRepository;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceWriteTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company existing(Long id) {
        Company c = new Company();
        ReflectionTestUtils.setField(c, "id", id);
        c.setName("Teknoloji A.S.");
        c.setSector("Yazilim");
        c.setContactEmail("info@teknoloji.com");
        c.setWebsite("https://teknoloji.com");
        c.setDescription("Eski aciklama");
        return c;
    }

    @Test
    void create_savesAndReturnsGeneratedId() {
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> {
            Company c = inv.getArgument(0);
            ReflectionTestUtils.setField(c, "id", 7L);
            return c;
        });

        CompanyResponse r = companyService.create(
                new CompanyRequest("Yeni A.S.", "Enerji", "a@b.com", null, null));

        assertEquals(7L, r.id());
        assertEquals("Yeni A.S.", r.name());
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void update_replacesAllFields() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing(1L)));
        when(companyRepository.update(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        CompanyResponse r = companyService.update(1L,
                new CompanyRequest("Yeni Ad", "Bilisim", "yeni@mail.com", "https://yeni.com", "Yeni aciklama"));

        assertEquals(1L, r.id());
        assertEquals("Yeni Ad", r.name());
        assertEquals("Bilisim", r.sector());
        assertEquals("yeni@mail.com", r.contactEmail());
        assertEquals("https://yeni.com", r.website());
        assertEquals("Yeni aciklama", r.description());
    }

    @Test
    void update_whenNotFound_throwsAndDoesNotUpdate() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.update(99L,
                new CompanyRequest("X", "Y", "x@y.com", null, null)));

        verify(companyRepository, never()).update(any(Company.class));
    }

    @Test
    void patch_changesOnlySentFields() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing(1L)));
        when(companyRepository.update(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        CompanyResponse r = companyService.patch(1L,
                new CompanyPatchRequest(null, "Yapay Zeka", null, null, null));

        assertEquals(1L, r.id());
        assertEquals("Yapay Zeka", r.sector());
        assertEquals("Teknoloji A.S.", r.name());
        assertEquals("info@teknoloji.com", r.contactEmail());
        assertEquals("https://teknoloji.com", r.website());
        assertEquals("Eski aciklama", r.description());
    }

    @Test
    void patch_whenNotFound_throws() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.patch(99L,
                new CompanyPatchRequest("Ad", null, null, null, null)));

        verify(companyRepository, never()).update(any(Company.class));
    }

    @Test
    void delete_whenNotFound_throwsAndDoesNotDelete() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.delete(99L));

        verify(companyRepository, never()).delete(any(Company.class));
    }
}