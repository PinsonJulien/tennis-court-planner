import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { map, Observable } from "rxjs";
import { RequestAction } from "./types/requests/request-action.enum";
import { ApiResponse } from "../dtos/api/api.response.dto";
import { RoleDTO } from "../dtos/roles/role.dto";

@Injectable({providedIn: 'root'})
export class RoleService extends ApiService {

    /******************************************************************************/
    /* Properties                                                                 */
    /******************************************************************************/

    protected override readonly apiRoute: string = "roles";

    /******************************************************************************/
    /* Constructors                                                               */
    /******************************************************************************/

    //

    /******************************************************************************/
    /* Methods                                                                    */
    /******************************************************************************/

    public findAll(): Observable<ApiResponse<RoleDTO[]>> {
        return this.request<RoleDTO[]>(RequestAction.GET, '');
    }

}