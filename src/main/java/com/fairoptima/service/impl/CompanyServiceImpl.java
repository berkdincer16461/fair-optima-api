package com.fairoptima.service.impl;

import com.fairoptima.dto.CompanyPatchRequest;
import com.fairoptima.dto.CompanyRequest;
import com.fairoptima.dto.CompanyResponse;
import com.fairoptima.entity.Company;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.CompanyRepository;
import com.fairoptima.repository.ReservationRepository;
import com.fairoptima.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ReservationRepository reservationRepository; // Yeni eklendi

    // Constructor güncellendi
    public CompanyServiceImpl(CompanyRepository companyRepository, ReservationRepository reservationRepository) {
        this.companyRepository = companyRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        Company company = new Company();
        apply(company, request);
        return toResponse(companyRepository.save(company));
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAll(String sector) {
        List<Company> companies = (sector == null || sector.isBlank())
                ? companyRepository.findAll()
                : companyRepository.findBySector(sector);
        return companies.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = findOrThrow(id);
        apply(company, request);
        return toResponse(companyRepository.update(company));
    }

    @Override
    @Transactional
    public CompanyResponse patch(Long id, CompanyPatchRequest request) {
        Company company = findOrThrow(id);
        if (request.name() != null) company.setName(request.name());
        if (request.sector() != null) company.setSector(request.sector());
        if (request.contactEmail() != null) company.setContactEmail(request.contactEmail());
        if (request.website() != null) company.setWebsite(request.website());
        if (request.description() != null) company.setDescription(request.description());
        return toResponse(companyRepository.update(company));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Company company = findOrThrow(id);
        
        // İş Kuralı: Şirketin aktif rezervasyonu varsa silinmesini engelle
        if (reservationRepository.countByCompanyId(id) > 0) {
            throw new BadRequestException("Bu şirkete ait rezervasyonlar bulunduğu için şirket silinemez.");
        }
        
        companyRepository.delete(company);
    }

    private Company findOrThrow(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Şirket", id));
    }

    private void apply(Company c, CompanyRequest r) {
        c.setName(r.name());
        c.setSector(r.sector());
        c.setContactEmail(r.contactEmail());
        c.setWebsite(r.website());
        c.setDescription(r.description());
    }

    private CompanyResponse toResponse(Company c) {
        return new CompanyResponse(
                c.getId(),
                c.getName(),
                c.getSector(),
                c.getContactEmail(),
                c.getWebsite(),
                c.getDescription(),
                c.getCreatedAt()
        );
    }
}