package com.fairoptima.repository;

import com.fairoptima.entity.AppUser;
import java.util.Optional;

public interface UserRepository {
    AppUser save(AppUser user);
    Optional<AppUser> findByUsername(String username);
    long count();
}