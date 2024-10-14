import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ApiResponse } from "../../../../dtos/api/api.response.dto";
import { CourtService } from "../../../../services/court.service";
import { CourtDTO } from "../../../../dtos/courts/court.dto";
import { BookingDTO } from "../../../../dtos/bookings/booking.dto";
import { FormsModule } from "@angular/forms";
import { catchError, map, of } from "rxjs";
import { CreateBookingForCourtDTO } from "../../../../dtos/courts/create-booking-for-court.dto";
import { DateHelper } from "../../../../helpers/date.helper";


/**************************************************************************
 * Types
 * ************************************************************************/

type BookingRange = {
    start: Date;
    end: Date;
    startHourString: string;
    endHourString: string;
    court: CourtDTO;
    disabled: boolean;
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
    ],
    providers: []
  })
export class CourtPage implements OnInit {

    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected courts: CourtDTO[] = [];

    protected selectedCourt: CourtDTO | null = null;
    protected selectedDate: Date = new Date();
    protected bookingRanges: BookingRange[] = [];


    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected courtService: CourtService,
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    ngOnInit(): void {
        this.refresh();
    }

    protected refresh() {
        this.fetchCourts();
    }

    protected fetchCourts() {
        this.courtService.findAll()
            .subscribe((response: ApiResponse<CourtDTO[]>) => {
                if (response.error) {
                    return;
                }

                this.courts = response.data!;
            });
    }

    protected onBookingToggle(range: BookingRange) {
        if (range.disabled)
            return;

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
                if (response.error) {
                    return;
                }

                this.selectedCourt = response.data!;
                this.generateBookingRanges();
            });
    }

    protected cancelBooking(range: BookingRange) {
        if (!range.ownedByUser)
            return;

        this.courtService.cancelBooking(range.court.id, range.booking!.id)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.error) {
                    return;
                }

                this.selectedCourt = response.data!;
                this.generateBookingRanges();
            });
    }

    protected onCourtChange(event: any) {
        const courtId = parseInt(event.target.value);

        this.courtService.findById(courtId)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.error) {
                    return;
                }

                this.selectedCourt = response.data!;
            });
    }

    protected onDateChange(event: any) {
        this.selectedDate = new Date(event.target.value);
        if (this.selectedCourt !== null)
            this.generateBookingRanges();
    }

    protected generateBookingRanges() {
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

            let disabled = (start < now);
            let ownedByUser = false;

            if (booking) {
                ownedByUser = this.isBookingOwnedByUser(booking);
                disabled = !ownedByUser;
            }           
        
            const range: BookingRange = {
                start,
                end,
                startHourString,
                endHourString,
                court,
                disabled,
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

    protected isBookingFromPast(booking: BookingDTO): boolean {
        return new Date(booking.startDateTime) < new Date();
    }

    protected isBookingOwnedByUser(booking: BookingDTO): boolean {
        // TODO : Get logged user and compare by id.
        return booking.user === null;
    }
}