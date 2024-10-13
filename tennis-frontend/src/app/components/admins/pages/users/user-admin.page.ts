import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ApiResponse } from "../../../../dtos/api/api.response.dto";
import { UserService } from "../../../../services/user.service";
import { RoleService } from "../../../../services/role.service";
import { UserDTO } from "../../../../dtos/users/user.dto";
import { RoleDTO } from "../../../../dtos/roles/role.dto";
import { AddRoleToUserDTO } from "../../../../dtos/users/add-role-to-user.dto";

@Component({
    standalone: true,
    selector: 'app-user-admin-page',
    templateUrl: 'user-admin.page.html',
    styleUrls: ['user-admin.page.scss'],
    imports: [
        CommonModule     
    ],
    providers: []
})
export class UserAdminPage implements OnInit {
    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected users: UserDTO[] = [];
    protected roles: RoleDTO[] = [];


    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected userService: UserService,
        protected roleService: RoleService
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    ngOnInit(): void {
        this.refresh();
        console.log('User Admin Page initialized');
        console.log('Users:', this.users);
        console.log('Roles:', this.roles);
    }

    protected refresh(): void {
        this.fetchUsers();
        this.fetchRoles();
    }

    protected fetchUsers(): void {
        this.userService.findAll().subscribe((response: ApiResponse<UserDTO[]>) => {
            if (response.error) {
                console.error('Error fetching users:', response.error);
                return;
            }

            this.users = response.data!;            
        });
    }

    protected fetchRoles(): void {
        this.roleService.findAll().subscribe((response: ApiResponse<RoleDTO[]>) => {
            if (response.error) {
                console.error('Error fetching roles:', response.error);
                return;
            }

            this.roles = response.data!;
        });
    }

    protected deleteUser(user: UserDTO): void {
        this.userService.delete(user.id).subscribe((response: ApiResponse<void>) => {
            if (response.error) {
                return;
            }

            this.refresh();
        });
    }

    protected addRoleToUser(user: UserDTO, role: RoleDTO): void {
        const addRoleToUserDto: AddRoleToUserDTO = new AddRoleToUserDTO(role.id);

        this.userService.addRoleToUser(user.id, addRoleToUserDto).subscribe((response: ApiResponse<UserDTO>) => {
            if (response.error) {
                return;
            }

            this.refresh();
        });
    }

    protected removeRoleFromUser(user: UserDTO, role: RoleDTO): void {
        this.userService.removeRoleFromUser(user.id, role.id).subscribe((response: ApiResponse<UserDTO>) => {
            if (response.error) {
                return;
            }

            this.refresh();
        });
    }

    protected hasRole(user: UserDTO, role: RoleDTO): boolean {
        return user.roles.some((userRole: RoleDTO) => userRole.id === role.id);
    }

    protected toggleRole(user: UserDTO, role: RoleDTO): void {
        if (this.hasRole(user, role)) {
            this.removeRoleFromUser(user, role);
        } else {
            this.addRoleToUser(user, role);
        }
    }

}