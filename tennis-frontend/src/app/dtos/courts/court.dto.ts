import { BookingDTO } from "../bookings/booking.dto";

export class CourtDTO {
    id: number = 0;
    name: string = "";
    description: string = "";
    address: string = "";
    imageUrl: string = "";
    createdAt: Date = new Date();
    updatedAt: Date = new Date();
    bookings: BookingDTO[] = [];

    constructor(data: Partial<CourtDTO>) {
        Object.assign(this, data);
    }
}