import { UserDTO } from "../users/user.dto";

export class BookingDTO {
    id: number = 0;
    user: UserDTO | null = null;
    startDateTime: Date = new Date();
    endDateTime: Date = new Date();
    createdAt: Date = new Date();

    constructor(data: Partial<BookingDTO>) {
        Object.assign(this, data);
    }
}