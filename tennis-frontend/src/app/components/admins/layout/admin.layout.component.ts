import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";

@Component({
    standalone: true,
    selector: 'app-admin-layout',
    templateUrl: 'admin.layout.component.html',
    styleUrls: ['./admin.layout.component.css'],
    imports: [
        CommonModule,
        RouterModule,
    ],
})
export class AdminLayout {
    //
}