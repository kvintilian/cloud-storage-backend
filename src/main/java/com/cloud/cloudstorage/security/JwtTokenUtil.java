package com.cloud.cloudstorage.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    public String generateToken(UserDetails userDetails){
        return null;
    }
}
