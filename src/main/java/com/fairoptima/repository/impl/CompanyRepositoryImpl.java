package com.fairoptima.repository.impl;

import com.fairoptima.entity.Company;
import com.fairoptima.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final EntityManager em;

    public CompanyRepositoryImpl(EntityManagerFactory emf) {
        this.em = SharedEntityManagerCreator.createSharedEntityManager(emf);
    }

    @Override
    public Company save(Company company) {
        em.persist(company);
        return company;
    }

    @Override
    public Optional<Company> findById(Long id) {
        return Optional.ofNullable(em.find(Company.class, id));
    }

    @Override
    public List<Company> findAll() {
        return em.createQuery("SELECT c FROM Company c ORDER BY c.id", Company.class)
                .getResultList();
    }

    @Override
    public Company update(Company company) {
        return em.merge(company);
    }

    @Override
    public void delete(Company company) {
        em.remove(em.contains(company) ? company : em.merge(company));
    }

    @Override
    public List<Company> findBySector(String sector) {
        return em.createQuery(
                        "SELECT c FROM Company c WHERE LOWER(c.sector) = LOWER(:sector) ORDER BY c.id",
                        Company.class)
                .setParameter("sector", sector)
                .getResultList();
    }
}