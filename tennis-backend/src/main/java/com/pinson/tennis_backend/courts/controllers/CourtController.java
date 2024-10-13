package com.pinson.tennis_backend.courts.controllers;

import com.pinson.tennis_backend.auths.services.IAuthService;
import com.pinson.tennis_backend.bookings.dtos.CreateBookingDTO;
import com.pinson.tennis_backend.commons.controllers.BaseController;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.courts.dtos.CourtDTO;
import com.pinson.tennis_backend.courts.dtos.CreateCourtDTO;
import com.pinson.tennis_backend.courts.dtos.requests.CreateBookingForCourtRequestDTO;
import com.pinson.tennis_backend.courts.dtos.requests.CreateCourtRequestDTO;
import com.pinson.tennis_backend.courts.services.ICourtService;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CourtController.class);

    @Autowired
    private ICourtService courtService;

    @Autowired
    private IAuthService authService;


    @GetMapping("/")
    public BaseApiResponse<List<CourtDTO>> index() {
        final String method = "courts.index";
        final List<CourtDTO> courts = this.courtService.findAll();

        return this.createResponse(
            HttpStatus.OK,
            method,
            courts
        );
    }

    @GetMapping("/{id}")
    public BaseApiResponse<CourtDTO> show(
        @PathVariable final Long id
    ) {
        final String method = "courts.show";
        try {
            final CourtDTO court = this.courtService.findById(id);

            return this.createResponse(
                HttpStatus.OK,
                method,
                court
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;
            final String domain = "Court";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

    @PostMapping("/")
    public BaseApiResponse<CourtDTO> create(
        @RequestBody final CreateCourtRequestDTO createCourtRequestDTO
    ) {
        final String method = "courts.create";
        try {
            final CreateCourtDTO createCourtDTO = createCourtRequestDTO.toCreateCourtDTO();
            final CourtDTO court = this.courtService.create(createCourtDTO);

            return this.createResponse(
                HttpStatus.CREATED,
                method,
                court
            );
        } catch (Exception e) {
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            final String domain = "Court";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

    @PostMapping("/{id}/bookings")
    public BaseApiResponse<CourtDTO> book(
        @PathVariable final Long id,
        @RequestBody final CreateBookingForCourtRequestDTO createBookingForCourtRequestDTO
    ) {
        final String method = "courts.bookings.create";
        try {
            // Get current user
            final UserDTO user = this.authService.getCurrentUser();

            final CreateBookingDTO createBookingDTO = createBookingForCourtRequestDTO.toCreateBookingDTO(user.id(), id);

            final CourtDTO court = this.courtService.book(createBookingDTO);

            return this.createResponse(
                HttpStatus.OK,
                method,
                court
            );
        } catch (Exception e) {
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            final String domain = "Court";

            return this.createExceptionResponse(
                status,
                method,
                domain,
                e
            );
        }
    }

}
