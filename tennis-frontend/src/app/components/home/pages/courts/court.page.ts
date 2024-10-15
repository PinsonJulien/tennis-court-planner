import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ApiErrorDTO, ApiResponse } from "../../../../dtos/api/api.response.dto";
import { CourtService } from "../../../../services/court.service";
import { CourtDTO } from "../../../../dtos/courts/court.dto";
import { BookingDTO } from "../../../../dtos/bookings/booking.dto";
import { FormsModule } from "@angular/forms";
import { CreateBookingForCourtDTO } from "../../../../dtos/courts/create-booking-for-court.dto";
import { AuthService } from "../../../../services/auth.service";
import { UserDTO } from "../../../../dtos/users/user.dto";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectChange, MatSelectModule } from "@angular/material/select";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatButtonModule } from "@angular/material/button";
import { MatNativeDateModule } from "@angular/material/core";
import { MatInputModule } from "@angular/material/input";
import { NotificationService } from "../../../../services/notification.service";


/**************************************************************************
 * Types
 * ************************************************************************/

type BookingRange = {
    start: Date;
    end: Date;
    startHourString: string;
    endHourString: string;
    court: CourtDTO;
    ownedByUser: boolean;
    booking: BookingDTO | null;
};


@Component({
    standalone: true,
    selector: 'app-cour-page',
    templateUrl: 'court.page.html',
    styleUrls: ['court.page.scss'],
    imports: [
      CommonModule,
      FormsModule,
      MatCardModule,
      MatFormFieldModule,
      MatInputModule,
      MatSelectModule,
      MatDatepickerModule,
      MatButtonModule,
      MatNativeDateModule
    ],
    providers: []
  })
export class CourtPage implements OnInit {

    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected courts: CourtDTO[] = [];

    protected selectedCourt: CourtDTO | null = null;
    protected selectedDate: Date | null = null;
    protected bookingRanges: BookingRange[] = [];
    protected currentUser: UserDTO | null = null;
    protected hasUserOwnedBooking: boolean = false;


    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected courtService: CourtService,
        protected authService: AuthService,
        private notificationService: NotificationService,
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    ngOnInit(): void {
        this.refresh();

        this.authService.currentUser.subscribe((user: UserDTO | null) => {
            this.currentUser = user;
        });
    }

    protected refresh() {
        this.fetchCourts();
    }

    protected fetchCourts() {
        this.courtService.findAll()
            .subscribe((response: ApiResponse<CourtDTO[]>) => {
                if (response.error) {
                    this.showErrorMessage(response.error!);
                    return;
                }

                this.courts = response.data!;
            });
    }

    protected onBookingToggle(range: BookingRange) {
        if (range.ownedByUser)
            this.cancelBooking(range);
        else 
            this.book(range);        
    }

    protected book(range: BookingRange) {
        const createBookingForCourtDTO = new CreateBookingForCourtDTO(
            range.start,
            range.end
        );

        this.courtService.book(range.court.id, createBookingForCourtDTO)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.data) {
                    this.selectedCourt = response.data;
                    this.showSuccessMessage('Booking created successfully');
                } else {
                    this.showErrorMessage(response.error!);
                }

                this.generateBookingRanges();
            });
    }

    protected cancelBooking(range: BookingRange) {
        if (!range.ownedByUser)
            return;

        this.courtService.cancelBooking(range.court.id, range.booking!.id)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.data) {
                    this.selectedCourt = response.data!;
                    this.hasUserOwnedBooking = false;
                    this.showSuccessMessage('Booking cancelled successfully');
                } else {
                    this.showErrorMessage(response.error!);
                }
                
                this.generateBookingRanges();
            });
    }

    protected onCourtChange(event: MatSelectChange) {
        const courtId = parseInt(event.value);

        this.courtService.findById(courtId)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.error) {
                    this.showErrorMessage(response.error!);
                    return;
                }
                this.hasUserOwnedBooking = false;
                this.selectedCourt = response.data!;
                this.generateBookingRanges();
            });
    }

    protected onDateChange(event: any) {
        this.selectedDate = new Date(event.target.value);
        this.hasUserOwnedBooking = false;
        this.generateBookingRanges();
    }

    protected generateBookingRanges() {
        if (this.selectedCourt === null || this.selectedDate === null)
            return;

        const court = this.selectedCourt!;
        const bookings = court.bookings;
        const ranges: BookingRange[] = [];

        // Generate all ranges for a given date : 00:00 - 01:00, 01:00 - 02:00, etc.
        for (let i = 0; i < 24; i++) {
            const now = new Date();
            const start = new Date(this.selectedDate);
            start.setHours(i);
            start.setMinutes(0);

            const end = new Date(start);
            end.setHours(i + 1);

            // format to dates to HH:MM format, e.g. 09:00 
            const startHourString = start.toTimeString().slice(0, 5);
            const endHourString = end.toTimeString().slice(0, 5);

            const booking = this.searchForBookingByRange(bookings, start, end);

            let ownedByUser = false;

            if (booking) {
                ownedByUser = this.isBookingOwnedByUser(booking);
                if (ownedByUser)
                    this.hasUserOwnedBooking = true;
            }           
        
            const range: BookingRange = {
                start,
                end,
                startHourString,
                endHourString,
                court,
                ownedByUser,
                booking
            };

            ranges.push(range);
        }

        this.bookingRanges = ranges;
    }

    protected searchForBookingByRange(bookings: BookingDTO[], start: Date, end: Date): BookingDTO | null {
        for (let booking of bookings) {
            const startDateTime = new Date(booking.startDateTime);
            const endDateTime = new Date(booking.endDateTime);
            if (startDateTime.getTime() === start.getTime() && endDateTime.getTime() === end.getTime())
                return booking;
        }

        return null
    }

    protected isBookingDisabled(range: BookingRange): boolean {
        // If from the past, disable
        if (range.start < new Date())
            return true;

        // If the user own the booking, enable
        if (range.ownedByUser)
            return false;

        // If the user own at least one booking from the same court at the same time, disable
        if (this.hasUserOwnedBooking)
            return true;

        // If the user does not own any booking, and the slot is already booked, disable
        if (range.booking !== null)
            return true;

        return false;
    }


    protected isBookingOwnedByUser(booking: BookingDTO): boolean {
        return this.currentUser !== null && booking.user !== null && booking.user.id === this.currentUser.id;
    }

    protected showErrorMessage(error: ApiErrorDTO) {
        this.notificationService.showErrorMessage(error.message);
    }

    protected showSuccessMessage(message: string) {
        this.notificationService.showSuccessMessage(message);
    }
}