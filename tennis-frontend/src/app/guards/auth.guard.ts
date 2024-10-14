import { Injectable } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";
import { map } from "rxjs";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) {
        //
    }

    canActivate(
        route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot
    ): MaybeAsync<GuardResult> {
        return this.authService.me().pipe(
            map((response) => {
                if (response.error) {
                    this.router.navigate(['auth/login']);
                    return false;
                }

                return true;
            })
        );
    }

}