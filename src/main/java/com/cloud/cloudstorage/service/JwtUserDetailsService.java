package com.cloud.cloudstorage.service;

import com.cloud.cloudstorage.entity.UserEntity;
import com.cloud.cloudstorage.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
        return new User(userEntity.getLogin(), userEntity.getPassword(), new ArrayList<>());
    }
}
