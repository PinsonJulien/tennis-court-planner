import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { RequestAction } from "./types/requests/request-action.enum";
import { RequestParameters } from "./types/requests/request-parameters.enum";
import { catchError, map, Observable, of } from "rxjs";
import { LocalStorageService } from "./local-storage.service";
import { ApiResponse } from "../dtos/api/api.response.dto";

@Injectable({providedIn: 'root'})
export abstract class ApiService {

    /******************************************************************************/
    /* Properties                                                                 */
    /******************************************************************************/

    protected readonly apiUrl = `${environment.apiUrl}/api`;

    protected readonly httpOptions = {
        headers: new HttpHeaders({
            //'X-Requested-With': 'XMLHttpRequest',
            'Accept' : 'application/json',
        }),
    };

    protected readonly apiRoute: string = "";

    /******************************************************************************/
    /* Constructors                                                               */
    /******************************************************************************/

    constructor(
        protected http: HttpClient,
        protected localStorage: LocalStorageService
    ) {
        //
    }

    /******************************************************************************/
    /* Methods                                                                    */
    /******************************************************************************/

    protected request<T extends (object|object[]|void)>(
        action: RequestAction,
        path: string|number,
        parameters: RequestParameters = {},
        body: Object = {}
    ): Observable<ApiResponse<T>> {
        const url = `${this.apiUrl}/${this.apiRoute}/${path}`;

        const params = new HttpParams({ fromObject: parameters });

        // Add the Authorization header if a token is present in local storage.
        const token = this.localStorage.getBearerToken();
        if (token) {
            this.httpOptions.headers = this.httpOptions.headers.set('Authorization', token);
        }

        const options = {
            ...this.httpOptions,
            params: params,
        };

        let response: Observable<T>;

        switch (action) {
            case RequestAction.GET :
                response = this.http.get<T>(url, options);    
                break;

            case RequestAction.POST :
                response = this.http.post<T>(url, body, options);
                break;

            case RequestAction.PUT :
                response = this.http.put<T>(url, body, options);
                break;

            case RequestAction.PATCH :
                response = this.http.patch<T>(url, body, options);    
                break;

            case RequestAction.DELETE :
                response = this.http.delete<T>(url, options);
                break;

            default :
                throw new Error(`Invalid HTTP action : ${action}`);
        }

        // Automatically map the response to an ApiResponse object
        return response.pipe(
            map((res: any) => {
                console.error(res);
                return new ApiResponse<T>(res);
            }),
            catchError((err) => {
                console.error(err);
                // On 401 Unauthorized, remove the token from local storage.
                if (err.status === 401) {
                    this.localStorage.removeBearerToken();
                }

                return of(new ApiResponse<T>(err.error));
            }),
        );

    }

}