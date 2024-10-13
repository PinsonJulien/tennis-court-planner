export class CreateBookingForCourtDTO {
    startDateTime: Date;
    endDateTime: Date;

    constructor(startDateTime: Date, endDateTime: Date) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}