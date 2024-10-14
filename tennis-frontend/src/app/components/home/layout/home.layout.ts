import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";

@Component({
    standalone: true,
    selector: 'app-home-layout',
    templateUrl: 'home.layout.html',
    styleUrls: ['./home.layout.css'],
    imports: [
        CommonModule,
        RouterModule,
    ],
})
export class HomeLayout {
    //
}