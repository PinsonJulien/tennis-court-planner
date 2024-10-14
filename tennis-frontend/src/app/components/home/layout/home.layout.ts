import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatToolbarModule } from "@angular/material/toolbar";
import { Router, RouterModule } from "@angular/router";
import { AuthService } from "../../../services/auth.service";

@Component({
    standalone: true,
    selector: 'app-home-layout',
    templateUrl: 'home.layout.html',
    styleUrls: ['./home.layout.css'],
    imports: [
        CommonModule,
        RouterModule,
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatListModule,
        MatIconModule,
    ],
})
export class HomeLayout {

    protected readonly links = [
        {
            route: '/courts',
            title: 'Our Courts',
        },
    ];

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
        //
    }

    protected logout() {
        this.authService.logout().subscribe(() => {
            // Redirect to login page
            this.router.navigate(['/auth/login']);
        });
    }

    
}