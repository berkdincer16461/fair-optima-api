package com.fairoptima.repository.impl;

import com.fairoptima.entity.Role;
import com.fairoptima.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role save(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
            return role;
        } else {
            return entityManager.merge(role);
        }
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            Role role = entityManager.createQuery(
                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(r) FROM Role r", Long.class)
                .getSingleResult();
    }
}