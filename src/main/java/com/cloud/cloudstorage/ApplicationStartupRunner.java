package com.cloud.cloudstorage;

import com.cloud.cloudstorage.entity.UserEntity;
import com.cloud.cloudstorage.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(UserEntity.builder().login("john").password(passwordEncoder.encode("doe")).build());
        userRepository.save(UserEntity.builder().login("vasiliy").password(passwordEncoder.encode("pupkin")).build());
    }
}
