package com.pinson.tennis_backend.courts.dtos.requests;

import com.pinson.tennis_backend.bookings.dtos.CreateBookingDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateBookingForCourtRequestDTO(
    LocalDateTime startDateTime,
    LocalDateTime endDateTime
) {
    public CreateBookingDTO toCreateBookingDTO(UUID userId, Long courtId) {
        return new CreateBookingDTO(userId, courtId, startDateTime, endDateTime);
    }
}
