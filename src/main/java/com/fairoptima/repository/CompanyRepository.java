package com.fairoptima.repository;

import com.fairoptima.entity.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findById(Long id);
    List<Company> findAll();
    Company update(Company company);
    void delete(Company company);
    List<Company> findBySector(String sector);
}