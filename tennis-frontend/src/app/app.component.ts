import { Component } from '@angular/core';
import { environment } from '../environments/environment';
import { RoleService } from './services/role.service';
import { ApiResponse } from './dtos/api/api.response.dto';
import { RoleDTO } from './dtos/roles/role.dto';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(
    private roleService: RoleService
  ) {
    console.log('Is environment production ?', environment.production);
    /*this.roleService.getAllRoles().subscribe((response: any) => {
      console.log(response);

      const responseDto: ApiResponse<RoleDTO[]> = new ApiResponse(response);
    });*/
  }

  title = 'tennis-frontend';
}
