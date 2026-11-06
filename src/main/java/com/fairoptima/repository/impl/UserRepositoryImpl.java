package com.fairoptima.repository.impl;

import com.fairoptima.entity.AppUser;
import com.fairoptima.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AppUser save(AppUser user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        try {
            AppUser user = entityManager.createQuery(
                    "SELECT u FROM AppUser u LEFT JOIN FETCH u.roles WHERE u.username = :username", AppUser.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM AppUser u", Long.class)
                .getSingleResult();
    }
}