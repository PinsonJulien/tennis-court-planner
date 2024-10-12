package com.pinson.tennis_backend.auths.providers;

import com.pinson.tennis_backend.auths.configurations.JwtConfiguration;
import com.pinson.tennis_backend.auths.dtos.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenProvider implements IJwtTokenProvider{
    @Autowired
    private JwtConfiguration jwtConfiguration;

    private final Set<String> blacklistedTokens = new HashSet<>();

    private SecretKey secretKey;

    /*************************************************************************
     * IJwtTokenProvider implementation
     *************************************************************************/

    @Override
    public JwtDTO generateToken(Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + this.jwtConfiguration.getExpiration());

        final String token = Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(this.getSigningKey())
            .compact();

        return JwtDTO.from(token, issuedAt, expiration);
    }

    @Override
    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser()
            .verifyWith(this.getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        if (this.isBlacklisted(token))
            return false;

        try {
            Jwts.parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void blacklistToken(String token) {
        if (!this.isBlacklisted(token))
            this.blacklistedTokens.add(token);
    }

    /*************************************************************************
     * Helper methods
     *************************************************************************/

    protected SecretKey getSigningKey() {
        if (this.secretKey == null)
            this.setSecretKey();

        return this.secretKey;
    }

    private void setSecretKey() {
        this.secretKey = Keys.hmacShaKeyFor(this.jwtConfiguration.getSecret().getBytes());
    }

    protected boolean isBlacklisted(String token) {
        return this.blacklistedTokens.contains(token);
    }

}
