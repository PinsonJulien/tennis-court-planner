import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { LoginDTO } from "../dtos/auths/login.dto";
import { map, Observable } from "rxjs";
import { ApiResponse } from "../dtos/api/api.response.dto";
import { AuthenticationDTO } from "../dtos/auths/authentication.dto";
import { RequestAction } from "./types/requests/request-action.enum";
import { SignupDTO } from "../dtos/auths/signup.dto";

@Injectable({providedIn: 'root'})
export class AuthService extends ApiService {
    /******************************************************************************/
    /* Properties                                                                 */
    /******************************************************************************/

    protected override readonly apiRoute: string = "auth";

    /******************************************************************************/
    /* Constructors                                                               */
    /******************************************************************************/

    //

    /******************************************************************************/
    /* Methods                                                                    */
    /******************************************************************************/

    public login(body: LoginDTO) : Observable<ApiResponse<AuthenticationDTO>> {
        return this.request<AuthenticationDTO>(RequestAction.POST, 'login', {}, body).pipe(
            map((response: ApiResponse<AuthenticationDTO>) => this.handleAuthentication(response))
        );
    }

    public logout() : Observable<ApiResponse<void>> {
        return this.request<void>(RequestAction.POST, 'logout');
    }

    public signup(body: SignupDTO) : Observable<ApiResponse<AuthenticationDTO>> {
        return this.request<AuthenticationDTO>(RequestAction.POST, 'signup', {}, body);
    }

    protected handleAuthentication(response: ApiResponse<AuthenticationDTO>): ApiResponse<AuthenticationDTO> {
        if (response.error) {
            return response;
        }

        // On success, store the token in local storage
        this.localStorage.setBearerToken(response.data!.jwt!.token);

        return response;
    }

}