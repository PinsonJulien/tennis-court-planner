package com.pinson.tennis_backend.courts.dtos;

import com.pinson.tennis_backend.bookings.dtos.BookingDTO;
import com.pinson.tennis_backend.courts.entities.Court;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record CourtDTO (
    Long id,
    String name,
    String description,
    String address,
    String imageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Optional<List<BookingDTO>> bookings
) {
    public static CourtDTO from(Court court) {
        return from(court, false);
    }

    public static CourtDTO from(Court court, boolean includeRelations) {
        final Long id = court.getId();
        final String name = court.getName();
        final String description = court.getDescription();
        final String address = court.getAddress();
        final String imageUrl = court.getImageUrl();
        final LocalDateTime createdAt = court.getCreatedAt();
        final LocalDateTime updatedAt = court.getUpdatedAt();
        Optional<List<BookingDTO>> bookings = Optional.empty();

        if (includeRelations) {
            bookings = Optional.of(BookingDTO.from(court.getBookings(), false));
        }

        return new CourtDTO(id, name, description, address, imageUrl, createdAt, updatedAt, bookings);
    }

    public static List<CourtDTO> from (Collection<Court> courts) {
        return from(courts, false);
    }

    public static List<CourtDTO> from (Collection<Court> courts, boolean includeRelations) {
        return courts.stream()
            .map(court -> from(court, includeRelations))
            .toList();
    }

}
