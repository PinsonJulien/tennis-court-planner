package com.pinson.tennis_backend.courts.services;

import com.pinson.tennis_backend.bookings.dtos.CreateBookingDTO;
import com.pinson.tennis_backend.bookings.entities.Booking;
import com.pinson.tennis_backend.bookings.repositories.IBookingRepository;
import com.pinson.tennis_backend.courts.dtos.CourtDTO;
import com.pinson.tennis_backend.courts.dtos.CreateCourtDTO;
import com.pinson.tennis_backend.courts.dtos.PartialUpdateCourtDTO;
import com.pinson.tennis_backend.courts.dtos.UpdateCourtDTO;
import com.pinson.tennis_backend.courts.entities.Court;
import com.pinson.tennis_backend.courts.repositories.ICourtRepository;
import com.pinson.tennis_backend.file_storages.services.IFileStorageService;
import com.pinson.tennis_backend.users.entities.User;
import com.pinson.tennis_backend.users.repositories.IUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class CourtService implements ICourtService {

    @Autowired
    private ICourtRepository courtRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IFileStorageService fileStorageService;

    /*************************************************************************
     * ICourtService implementation
     *************************************************************************/

    @Override
    public List<CourtDTO> findAll() {
        final List<Court> courts = this.courtRepository.findAll();
        return CourtDTO.from(courts);
    }

    @Override
    public CourtDTO findById(Long id) {
        final Court court = this.courtRepository.findById(id).orElseThrow();
        return CourtDTO.from(court);
    }

    @Override
    public CourtDTO create(CreateCourtDTO createCourtDTO) {
        final Court newCourt = Court.builder()
            .name(createCourtDTO.name())
            .description(createCourtDTO.description())
            .address(createCourtDTO.address())
            // As of now, we don't store the image locally, we store the image URL from external websites.
            // This solution is temporary and will be replaced with a proper file storage solution using the FileStorageService.
            .imageUrl(createCourtDTO.imageUrl())
            .build();

        final Court createdCourt = this.courtRepository.save(newCourt);
        return CourtDTO.from(createdCourt);
    }

    @Override
    public CourtDTO update(Long id, UpdateCourtDTO updateCourtDTO) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public CourtDTO partialUpdate(
        Long id,
        @Valid PartialUpdateCourtDTO partialUpdateCourtDTO
    ) {
        final Court court = this.courtRepository.findById(id).orElseThrow();

        if (partialUpdateCourtDTO.isNameSet())
            court.setName(partialUpdateCourtDTO.getName());
        if (partialUpdateCourtDTO.isDescriptionSet())
            court.setDescription(partialUpdateCourtDTO.getDescription());
        if (partialUpdateCourtDTO.isAddressSet())
            court.setAddress(partialUpdateCourtDTO.getAddress());
        if (partialUpdateCourtDTO.isImageUrlSet())
            court.setImageUrl(partialUpdateCourtDTO.getImageUrl());

        final Court updatedCourt = this.courtRepository.save(court);
        return CourtDTO.from(updatedCourt);
    }

    @Override
    public void delete(Long id) {
        final Court court = this.courtRepository.findById(id).orElseThrow();
        this.courtRepository.delete(court);
    }

    @Override
    public CourtDTO book(CreateBookingDTO createBookingDTO) {
        final Court court = this.courtRepository.findById(createBookingDTO.courtId()).orElseThrow();
        final User user = this.userRepository.findById(createBookingDTO.userId()).orElseThrow();

        // Check if the user has already booked a court for the given startDateTime
        if (this.canUserBookACourtAtDateTime(user, createBookingDTO.startDateTime()))
            throw new IllegalArgumentException("User has already booked a court for the given startDateTime");

        // Check if the startDateTime is valid
        if (!this.isLocalDateTimeValid(createBookingDTO.startDateTime()))
            throw new IllegalArgumentException("Invalid startDateTime, does not allow minutes");

        // Check if the endDateTime is valid
        if (!this.isLocalDateTimeValid(createBookingDTO.endDateTime()))
            throw new IllegalArgumentException("Invalid endDateTime, does not allow minutes");

        // Check if the duration of the booking is valid (1 hour)
        if (!this.isBookingDurationValid(createBookingDTO.startDateTime(), createBookingDTO.endDateTime()))
            throw new IllegalArgumentException("Invalid booking duration, should be 1 hour");

        // Check if the court is available for the given startDateTime and endDateTime
        if (!this.isCourtAvailable(court, createBookingDTO.startDateTime(), createBookingDTO.endDateTime()))
            throw new IllegalArgumentException("Court is not available for the given startDateTime and endDateTime");


        final Booking booking = Booking.builder()
            .court(court)
            .user(user)
            .startDateTime(createBookingDTO.startDateTime())
            .endDateTime(createBookingDTO.endDateTime())
            .build();

        court.addBooking(booking);
        final Court updatedCourt = this.courtRepository.save(court);

        return CourtDTO.from(updatedCourt, true);
    }

    /*************************************************************************
     * Helper methods
     *************************************************************************/

    protected boolean canUserBookACourtAtDateTime(User user, LocalDateTime startDateTime) {
        // Users can only book one court per day.

        // Get earliest datetime of the startDateTime
        final LocalDate startDate = startDateTime.toLocalDate();
        final LocalDateTime earliestDateTime = LocalDateTime.of(startDate, LocalTime.MIDNIGHT);
        // Get the day after the startDateTime
        final LocalDate endDate = startDate.plusDays(1);
        final LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIDNIGHT);

        return this.bookingRepository.existsByUserAndStartDateTimeBetween(
            user,
            earliestDateTime,
            endDateTime
        );
    }

    protected boolean isLocalDateTimeValid(LocalDateTime localDateTime) {
        // only allows times like 10:00, 11:00, 12:00, etc.
        return localDateTime.getMinute() == 0;
    }

    protected boolean isBookingDurationValid(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // A booking should be 1 hour long
        final LocalDateTime oneHourAfterStartDateTime = startDateTime.plusHours(1);
        return oneHourAfterStartDateTime.isEqual(endDateTime);
    }

    protected boolean isCourtAvailable(Court court, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return court.getBookings().stream()
            .noneMatch(booking -> {
                final LocalDateTime bookingStartDateTime = booking.getStartDateTime();
                final LocalDateTime bookingEndDateTime = booking.getEndDateTime();
                return bookingEndDateTime.isAfter(startDateTime) && bookingStartDateTime.isBefore(endDateTime);
            });
    }


}
