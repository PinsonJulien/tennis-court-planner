import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { Observable } from "rxjs";
import { RequestAction } from "./types/requests/request-action.enum";
import { ApiResponse } from "../dtos/api/api.response.dto";
import { CourtDTO } from "../dtos/courts/court.dto";
import { UserDTO } from "../dtos/users/user.dto";
import { AddRoleToUserDTO } from "../dtos/users/add-role-to-user.dto";

@Injectable({providedIn: 'root'})
export class UserService extends ApiService {

    /******************************************************************************/
    /* Properties                                                                 */
    /******************************************************************************/

    protected override readonly apiRoute: string = "users";

    /******************************************************************************/
    /* Constructors                                                               */
    /******************************************************************************/

    //

    /******************************************************************************/
    /* Methods                                                                    */
    /******************************************************************************/

    public findAll(): Observable<ApiResponse<UserDTO[]>> {
        return this.request<UserDTO[]>(RequestAction.GET, '');
    }

    public findById(id: string): Observable<ApiResponse<CourtDTO>> {
        return this.request<CourtDTO>(RequestAction.GET, id);
    }

    public findAllDeleted(): Observable<ApiResponse<UserDTO[]>> {
        return this.request<UserDTO[]>(RequestAction.GET, 'deleted');
    }

    public delete(id: string): Observable<ApiResponse<void>> {
        return this.request<void>(RequestAction.DELETE, id);
    }

    public restore(id: string): Observable<ApiResponse<void>> {
        return this.request<void>(RequestAction.POST, `${id}/restore`);
    }

    public addRoleToUser(id: string, addRoleToUserDto: AddRoleToUserDTO): Observable<ApiResponse<UserDTO>> {
        return this.request<UserDTO>(RequestAction.POST, `${id}/roles`, {}, addRoleToUserDto);
    }

    public removeRoleFromUser(id: string, roleId: number): Observable<ApiResponse<UserDTO>> {
        return this.request<UserDTO>(RequestAction.DELETE, `${id}/roles/${roleId}`);
    }


}