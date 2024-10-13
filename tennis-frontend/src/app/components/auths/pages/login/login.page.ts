import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { AuthService } from "../../../../services/auth.service";
import { LoginDTO } from "../../../../dtos/auths/login.dto";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiResponse } from "../../../../dtos/api/api.response.dto";
import { AuthenticationDTO } from "../../../../dtos/auths/authentication.dto";
import { Router } from "@angular/router";


@Component({
    standalone: true,
    selector: 'app-login-page',
    templateUrl: 'login.page.html',
    styleUrls: ['login.page.scss'],
    imports: [
      CommonModule,
      ReactiveFormsModule,      
    ],
    providers: []
  })
export class LoginPage {
    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected username = new FormControl<string>('', [Validators.required]);
    protected password = new FormControl<string>('', [Validators.required]);

    protected formGroup: FormGroup = new FormGroup({
        username: this.username,
        password: this.password,
    });

    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected authService: AuthService,
        protected router: Router
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    protected onSubmit() {
        if (this.formGroup.invalid) {
            return;
        }

        const body: LoginDTO = {
            username: this.username.value ?? '',
            password: this.password.value ?? '',
        };

        this.authService.login(body)
            .subscribe((response: ApiResponse<AuthenticationDTO>) => {
                // if contains error
                if (response.error) {
                    return;
                }

                // redirect to home page
                this.router.navigate(['home']);
            });
    }

}