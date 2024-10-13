import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { map, Observable } from "rxjs";
import { RequestAction } from "./types/requests/request-action.enum";
import { ApiResponse } from "../dtos/api/api.response.dto";
import { CourtDTO } from "../dtos/courts/court.dto";
import { CreateCourtDTO } from "../dtos/courts/create-court.dto";
import { PartialUpdateCourtDTO } from "../dtos/courts/partial-update-court.dto";
import { CreateBookingForCourtDTO } from "../dtos/courts/create-booking-for-court.dto";

@Injectable({providedIn: 'root'})
export class CourtService extends ApiService {

    /******************************************************************************/
    /* Properties                                                                 */
    /******************************************************************************/

    protected override readonly apiRoute: string = "courts";

    /******************************************************************************/
    /* Constructors                                                               */
    /******************************************************************************/

    //

    /******************************************************************************/
    /* Methods                                                                    */
    /******************************************************************************/

    public findAll(): Observable<ApiResponse<CourtDTO[]>> {
        return this.request<CourtDTO[]>(RequestAction.GET, '');
    }

    public findById(id: number): Observable<ApiResponse<CourtDTO>> {
        return this.request<CourtDTO>(RequestAction.GET, id);
    }

    public create(body: CreateCourtDTO): Observable<ApiResponse<CourtDTO>> {
        return this.request<CourtDTO>(RequestAction.POST, '', {}, body);
    }

    public edit(id: number, body: PartialUpdateCourtDTO): Observable<ApiResponse<CourtDTO>> {
        return this.request<CourtDTO>(RequestAction.PATCH, id, {}, body);
    }

    public delete(id: number): Observable<ApiResponse<void>> {
        return this.request<void>(RequestAction.DELETE, id);
    }

    public book(id: number, body: CreateBookingForCourtDTO): Observable<ApiResponse<CourtDTO>> {
        return this.request<CourtDTO>(RequestAction.POST, `${id}/book`, {}, body);
    }

}