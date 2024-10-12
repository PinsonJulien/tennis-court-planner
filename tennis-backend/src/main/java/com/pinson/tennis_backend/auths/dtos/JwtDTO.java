package com.pinson.tennis_backend.auths.dtos;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public record JwtDTO(
    String token,
    String type,
    LocalDateTime issuedAt,
    LocalDateTime expiration
) {
    public static JwtDTO from(String token, Date issuedAt, Date expiration) {
        // Convert Dates to LocalDateTime
        final LocalDateTime issuedAtLDT = issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        final LocalDateTime expirationLDT = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new JwtDTO(
            token,
            "Bearer",
            issuedAtLDT,
            expirationLDT
        );
    }
}
