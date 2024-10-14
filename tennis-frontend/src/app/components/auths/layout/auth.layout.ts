import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";

@Component({
    standalone: true,
    selector: 'app-auth-layout',
    templateUrl: 'auth.layout.html',
    styleUrls: ['./auth.layout.css'],
    imports: [
        CommonModule,
        RouterModule,
    ],
})
export class AuthLayout {
    //
}