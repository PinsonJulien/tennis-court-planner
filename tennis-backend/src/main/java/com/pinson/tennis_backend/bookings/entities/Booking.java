package com.pinson.tennis_backend.bookings.entities;


import com.pinson.tennis_backend.courts.entities.Court;
import com.pinson.tennis_backend.users.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    /**************************************************************************
     * Fields
     **************************************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name="end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**************************************************************************
     * Helper methods
     **************************************************************************/

}
