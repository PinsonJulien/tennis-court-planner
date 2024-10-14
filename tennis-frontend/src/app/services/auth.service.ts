import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { LoginDTO } from "../dtos/auths/login.dto";
import { BehaviorSubject, Observable, tap } from "rxjs";
import { ApiResponse } from "../dtos/api/api.response.dto";
import { AuthenticationDTO } from "../dtos/auths/authentication.dto";
import { RequestAction } from "./types/requests/request-action.enum";
import { SignupDTO } from "../dtos/auths/signup.dto";
import { UserDTO } from "../dtos/users/user.dto";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "./local-storage.service";

@Injectable({providedIn: 'root'})
export class AuthService extends ApiService {
    /******************************************************************************/
    /* Properties                                                                 */
    /******************************************************************************/

    protected override readonly apiRoute: string = "auth";

    private currentUserSubject: BehaviorSubject<UserDTO|null>;
    public currentUser: Observable<UserDTO|null>;


    /******************************************************************************/
    /* Constructors                                                               */
    /******************************************************************************/

    constructor(
        protected override http: HttpClient,
        protected override localStorage: LocalStorageService
    ) {
        super(http, localStorage);

        this.currentUserSubject = new BehaviorSubject<UserDTO|null>(null);
        this.currentUser = this.currentUserSubject.asObservable();
    }

    /******************************************************************************/
    /* Methods                                                                    */
    /******************************************************************************/

    public login(body: LoginDTO) : Observable<ApiResponse<AuthenticationDTO>> {
        return this.request<AuthenticationDTO>(RequestAction.POST, 'login', {}, body).pipe(
            tap((response: ApiResponse<AuthenticationDTO>) => this.handleAuthentication(response))
        );
    }

    public logout() : Observable<ApiResponse<void>> {
        return this.request<void>(RequestAction.POST, 'logout').pipe(
            tap((response: ApiResponse<void>) => this.handleLogout(response))
        );
    }

    public signup(body: SignupDTO) : Observable<ApiResponse<AuthenticationDTO>> {
        return this.request<AuthenticationDTO>(RequestAction.POST, 'signup', {}, body).pipe(
            tap((response: ApiResponse<AuthenticationDTO>) => this.handleAuthentication(response))
        );
    }

    public me(): Observable<ApiResponse<UserDTO>> {
        return this.request<UserDTO>(RequestAction.GET, 'me').pipe(
            tap((response: ApiResponse<UserDTO>) => {
                // If the response is an error, deauthenticate the user.
                if (response.error) {
                    this.currentUserSubject.next(null);
                    this.deauthenticate();
                    return;
                }

                // Store the current user in the subject
                this.currentUserSubject.next(response.data!);
            })
        );
    }

    protected handleAuthentication(response: ApiResponse<AuthenticationDTO>) {
        if (response.error) {
            return;
        }

        // On success, store the token in local storage
        const token = response.data!.jwt!.token;
        this.authenticate(token);
    }

    protected handleLogout(response: ApiResponse<void>) {
        if (response.error) {
            return;
        }

        // On success, remove the token from local storage
        this.deauthenticate();
    }

    protected authenticate(token: string) {
        this.localStorage.setBearerToken(token);
    }

    protected deauthenticate() {
        this.localStorage.removeBearerToken();
    }

    public getCurrentUser(): Observable<UserDTO|null> {
        return this.currentUser;
    }

}