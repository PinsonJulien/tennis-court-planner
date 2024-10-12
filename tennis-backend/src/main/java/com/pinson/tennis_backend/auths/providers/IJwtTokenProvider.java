package com.pinson.tennis_backend.auths.providers;

import com.pinson.tennis_backend.auths.dtos.JwtDTO;
import org.springframework.security.core.Authentication;

public interface IJwtTokenProvider {

    JwtDTO generateToken(Authentication authentication);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    void blacklistToken(String token);
}
