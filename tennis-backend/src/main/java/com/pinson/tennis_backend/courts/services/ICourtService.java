package com.pinson.tennis_backend.courts.services;

import com.pinson.tennis_backend.bookings.dtos.CreateBookingDTO;
import com.pinson.tennis_backend.courts.dtos.CourtDTO;
import com.pinson.tennis_backend.courts.dtos.CreateCourtDTO;
import com.pinson.tennis_backend.courts.dtos.PartialUpdateCourtDTO;
import com.pinson.tennis_backend.courts.dtos.UpdateCourtDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

public interface ICourtService {
    List<CourtDTO> findAll();
    CourtDTO findById(Long id);
    CourtDTO create(CreateCourtDTO createCourtDTO);
    CourtDTO update(Long id, UpdateCourtDTO updateCourtDTO);
    CourtDTO partialUpdate(Long id, PartialUpdateCourtDTO partialUpdateCourtDTO);
    void delete(Long id);

    CourtDTO book(CreateBookingDTO createBookingDTO);

    CourtDTO cancelBooking(Long id, Long bookingId, UserDTO userDTO);
}
