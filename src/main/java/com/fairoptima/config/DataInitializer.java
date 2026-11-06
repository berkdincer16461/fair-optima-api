package com.fairoptima.config;

import com.fairoptima.entity.AppUser;
import com.fairoptima.entity.Role;
import com.fairoptima.repository.RoleRepository;
import com.fairoptima.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            // 1. Rolleri Olustur
            Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
            Role companyRole = roleRepository.save(new Role("ROLE_COMPANY"));
            Role readonlyRole = roleRepository.save(new Role("ROLE_READONLY"));

            // 2. Admin Kullanicisi (Tum yetkiler)
            AppUser admin = new AppUser("admin", passwordEncoder.encode("admin123"), true);
            admin.getRoles().add(adminRole);
            userRepository.save(admin);

            // 3. Company Kullanicisi (GET, POST, PUT, PATCH yetkisi)
            AppUser company = new AppUser("company", passwordEncoder.encode("company123"), true);
            company.getRoles().add(companyRole);
            userRepository.save(company);

            // 4. Readonly Kullanicisi (Sadece GET okuma yetkisi)
            AppUser readonlyUser = new AppUser("readonly", passwordEncoder.encode("readonly123"), true);
            readonlyUser.getRoles().add(readonlyRole);
            userRepository.save(readonlyUser);

            // 5. Pasif Kullanici Testi (enabled = false, 401 kontrolu icin)
            AppUser disabledUser = new AppUser("passive", passwordEncoder.encode("passive123"), false);
            disabledUser.getRoles().add(readonlyRole);
            userRepository.save(disabledUser);

            System.out.println(">>> Baslangic kullanicilari ve rolleri Argon2id ile basariyla olusturuldu!");
        }
    }
}