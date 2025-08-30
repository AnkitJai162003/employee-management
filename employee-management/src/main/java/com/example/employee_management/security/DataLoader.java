package com.example.employee_management.security;

import com.example.employee_management.entity.User;
import com.example.employee_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (!userRepo.existsByUsername("admin")) {
            userRepo.save(User.builder()
                    .username("admin")
                    .password(encoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build());
        }
        if (!userRepo.existsByUsername("user")) {
            userRepo.save(User.builder()
                    .username("user")
                    .password(encoder.encode("user123"))
                    .role("ROLE_USER")
                    .build());
        }
    }
}
