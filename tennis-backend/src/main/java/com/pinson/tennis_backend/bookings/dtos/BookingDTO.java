package com.pinson.tennis_backend.bookings.dtos;

import com.pinson.tennis_backend.bookings.entities.Booking;
import com.pinson.tennis_backend.users.dtos.UserDTO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public record BookingDTO(
    Long id,
    UserDTO user,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    LocalDateTime createdAt
) {
    public static BookingDTO from(Booking booking) {
        return from(booking, true);
    }

    public static BookingDTO from(Booking booking, boolean includeRelations) {
        final Long id = booking.getId();
        final UserDTO user = UserDTO.from(booking.getUser(), false);
        final LocalDateTime startDateTime = booking.getStartDateTime();
        final LocalDateTime endDateTime = booking.getEndDateTime();
        final LocalDateTime createdAt = booking.getCreatedAt();

        return new BookingDTO(id, user, startDateTime, endDateTime, createdAt);
    }

    public static List<BookingDTO> from(Collection<Booking> bookings) {
        return from(bookings, true);
    }

    public static List<BookingDTO> from(Collection<Booking> bookings, boolean includeRelations) {
        return bookings.stream()
            .map(booking -> from(booking, includeRelations))
            .toList();
    }

}
