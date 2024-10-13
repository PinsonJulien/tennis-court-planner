package com.pinson.tennis_backend.bookings.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateBookingDTO(
    UUID userId,
    Long courtId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime
) {
}
