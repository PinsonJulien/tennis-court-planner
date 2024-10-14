import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { AuthService } from "../../../../services/auth.service";
import { LoginDTO } from "../../../../dtos/auths/login.dto";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiResponse } from "../../../../dtos/api/api.response.dto";
import { AuthenticationDTO } from "../../../../dtos/auths/authentication.dto";
import { Router } from "@angular/router";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatDividerModule } from "@angular/material/divider";
import { SignupDTO } from "../../../../dtos/auths/signup.dto";
import { NotificationService } from "../../../../services/notification.service";


@Component({
    standalone: true,
    selector: 'app-login-page',
    templateUrl: 'login.page.html',
    styleUrls: ['login.page.scss'],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatDividerModule 
    ],
    providers: []
  })
export class LoginPage {
    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected loginForm: FormGroup;
    protected registerForm: FormGroup;
    
    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected authService: AuthService,
        protected notificationService: NotificationService,
        protected router: Router,
        private fb: FormBuilder
    ) {
        this.loginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    
        this.registerForm = this.fb.group({
            username: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    protected onLogin() {
        if (this.loginForm.invalid) {
            return;
        }

        const body: LoginDTO = {
            username: this.loginForm.get('username')?.value ?? '',
            password: this.loginForm.get('password')?.value ?? ''
        };

        this.authService.login(body)
            .subscribe((response: ApiResponse<AuthenticationDTO>) => {
                // if contains error
                if (response.error) {
                    this.notificationService.showErrorMessage(response.error.message);
                    return;
                }

                // redirect to home page
                this.router.navigate(['home']);
            });
    }

    protected onRegister() {
        if (this.registerForm.invalid) {
            return;
        }

        const body: SignupDTO = new SignupDTO(
            this.registerForm.get('username')?.value ?? '',
            this.registerForm.get('email')?.value ?? '',
            this.registerForm.get('password')?.value ?? ''
        );

        this.authService.signup(body)
            .subscribe((response: ApiResponse<AuthenticationDTO>) => {
                // if contains error
                if (response.error) {
                    this.notificationService.showErrorMessage(response.error.message);
                    return;
                }

                // redirect to home page
                this.router.navigate(['home']);
            });
    }

}