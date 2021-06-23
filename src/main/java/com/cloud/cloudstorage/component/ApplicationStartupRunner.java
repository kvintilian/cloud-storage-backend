package com.cloud.cloudstorage.component;

import com.cloud.cloudstorage.entity.UserEntity;
import com.cloud.cloudstorage.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    public ApplicationStartupRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(UserEntity.builder().login("john").password("doe").build());
        userRepository.save(UserEntity.builder().login("vasiliy").password("pupkin").build());
    }
}
