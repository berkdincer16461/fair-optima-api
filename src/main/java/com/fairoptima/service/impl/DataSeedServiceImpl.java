package com.fairoptima.service.impl;

import com.fairoptima.entity.AppUser;
import com.fairoptima.entity.Role;
import com.fairoptima.repository.RoleRepository;
import com.fairoptima.repository.UserRepository;
import com.fairoptima.service.DataSeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class DataSeedServiceImpl implements DataSeedService {

    private static final Logger log = LoggerFactory.getLogger(DataSeedServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeedServiceImpl(UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void seedIfEmpty() {
        if (roleRepository.count() == 0) {
            log.info("Veritabani bos, varsayilan roller ve kullanicilar olusturuluyor...");

            Role readonly = roleRepository.save(new Role("ROLE_READONLY"));
            Role company = roleRepository.save(new Role("ROLE_COMPANY"));
            Role admin = roleRepository.save(new Role("ROLE_ADMIN"));

            createUserIfNotFound("reader", "reader123", true, Set.of(readonly));
            createUserIfNotFound("company", "company123", true, Set.of(company));
            createUserIfNotFound("admin", "admin123", true, Set.of(admin));
            createUserIfNotFound("passive", "passive123", false, Set.of(company));

            log.info("Varsayilan tohum veriler basariyla eklendi.");
        }
    }

    private void createUserIfNotFound(String username, String rawPassword, boolean enabled, Set<Role> roles) {
        if (userRepository.findByUsername(username).isEmpty()) {
            AppUser user = new AppUser(username, passwordEncoder.encode(rawPassword), enabled);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
}