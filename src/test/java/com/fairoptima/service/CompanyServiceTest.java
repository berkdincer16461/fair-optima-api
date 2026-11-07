package com.fairoptima.service;

import com.fairoptima.dto.CompanyResponse;
import com.fairoptima.entity.Company;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.CompanyRepository;
import com.fairoptima.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import com.fairoptima.repository.ReservationRepository;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;
    

    @Mock
    private ReservationRepository reservationRepository; 

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company sampleCompany;

    @BeforeEach
    void setUp() {
        sampleCompany = new Company();
        ReflectionTestUtils.setField(sampleCompany, "id", 1L);
        sampleCompany.setName("Innova Bilisim");
        sampleCompany.setSector("Yazilim");
        sampleCompany.setContactEmail("info@innova.com");
        sampleCompany.setWebsite("https://innova.com");
        sampleCompany.setDescription("Kurumsal yazilim cozumleri");
    }

    @Test
    @DisplayName("Var olan kimlikle sirket basariyla getirilmeli")
    void shouldFindCompanyByIdWhenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(sampleCompany));

        CompanyResponse response = companyService.getById(1L);

        assertNotNull(response);
        assertEquals("Innova Bilisim", response.name());
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Olmayan kimlik arandiginda ResourceNotFoundException firlatmali")
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.getById(999L));
        verify(companyRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Tum sirketler sektor filtresi olmadan listelenebilmeli")
    void shouldListAllCompaniesWithoutFilter() {
        when(companyRepository.findAll()).thenReturn(List.of(sampleCompany));

        List<CompanyResponse> list = companyService.getAll(null);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Innova Bilisim", list.get(0).name());
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Sektor filtresine gore sirketler listelenebilmeli")
    void shouldListCompaniesBySector() {
        when(companyRepository.findBySector("Yazilim")).thenReturn(List.of(sampleCompany));

        List<CompanyResponse> list = companyService.getAll("Yazilim");

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Yazilim", list.get(0).sector());
        verify(companyRepository, times(1)).findBySector("Yazilim");
    }

    @Test
    @DisplayName("Var olan sirket basariyla silinmeli")
    void shouldDeleteCompanyWhenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(sampleCompany));
        doNothing().when(companyRepository).delete(sampleCompany);

        assertDoesNotThrow(() -> companyService.delete(1L));

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).delete(sampleCompany);
    }
}