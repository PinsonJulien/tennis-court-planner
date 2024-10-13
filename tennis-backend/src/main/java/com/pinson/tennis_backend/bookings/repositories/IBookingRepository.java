package com.pinson.tennis_backend.bookings.repositories;

import com.pinson.tennis_backend.bookings.entities.Booking;
import com.pinson.tennis_backend.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUserAndStartDateTimeBetween(
        @Param("user")
        User user,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    );
}
