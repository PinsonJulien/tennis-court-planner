package com.pinson.tennis_backend.courts.entities;

import com.pinson.tennis_backend.bookings.entities.Booking;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "courts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Court {

    /**************************************************************************
     * Fields
     **************************************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = true)
    private String description;

    @Column(name="address", nullable = false)
    private String address;

    // image
    @Column(name="image_url", nullable = true)
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(
        mappedBy = "court",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<Booking> bookings = new LinkedList<>();

    /**************************************************************************
     * Helper methods
     **************************************************************************/

    public List<Booking> getBookingsFromTodayAndFuture() {

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Filter bookings from today and future
        return this.bookings.stream()
            .filter(booking -> {
                final LocalDateTime startDateTime = booking.getStartDateTime();
                final boolean isSameDay = startDateTime.toLocalDate().isEqual(currentDateTime.toLocalDate());
                final boolean isFuture = startDateTime.isAfter(currentDateTime);
                return isSameDay || isFuture;
            })
            .toList();
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

}
