package com.fairoptima.repository;

import com.fairoptima.entity.Role;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findByName(String name);
    long count();
}