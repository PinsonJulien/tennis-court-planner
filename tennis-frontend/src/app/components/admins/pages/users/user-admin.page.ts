import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ApiErrorDTO, ApiResponse } from "../../../../dtos/api/api.response.dto";
import { UserService } from "../../../../services/user.service";
import { RoleService } from "../../../../services/role.service";
import { UserDTO } from "../../../../dtos/users/user.dto";
import { RoleDTO } from "../../../../dtos/roles/role.dto";
import { AddRoleToUserDTO } from "../../../../dtos/users/add-role-to-user.dto";
import { MatTableModule } from "@angular/material/table";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from "@angular/material/card";
import { NotificationService } from "../../../../services/notification.service";

@Component({
    standalone: true,
    selector: 'app-user-admin-page',
    templateUrl: 'user-admin.page.html',
    styleUrls: ['user-admin.page.scss'],
    imports: [
        CommonModule,
        MatTableModule,
        MatCheckboxModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
    ],
    providers: []
})
export class UserAdminPage implements OnInit {
    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected users: UserDTO[] = [];
    protected deletedUsers: UserDTO[] = [];
    protected roles: RoleDTO[] = [];


    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        private userService: UserService,
        private roleService: RoleService,
        private notificationService: NotificationService,
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    ngOnInit(): void {
        this.refresh();
    }

    protected refresh(): void {
        this.fetchUsers();
        this.fetchDeletedUsers();
        this.fetchRoles();
    }

    protected fetchUsers(): void {
        this.userService.findAll().subscribe((response: ApiResponse<UserDTO[]>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.users = response.data!;            
        });
    }

    protected fetchDeletedUsers(): void {
        this.userService.findAllDeleted().subscribe((response: ApiResponse<UserDTO[]>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.deletedUsers = response.data!;
        });
    }

    protected fetchRoles(): void {
        this.roleService.findAll().subscribe((response: ApiResponse<RoleDTO[]>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.roles = response.data!;
        });
    }

    protected deleteUser(user: UserDTO): void {
        this.userService.delete(user.id).subscribe((response: ApiResponse<void>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.refresh();
            this.showSuccessMessage('User deleted successfully');
        });
    }

    protected restoreUser(user: UserDTO): void {
        this.userService.restore(user.id).subscribe((response: ApiResponse<void>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.refresh();
            this.showSuccessMessage('User restored successfully');
        });
    }

    protected addRoleToUser(user: UserDTO, role: RoleDTO): void {
        const addRoleToUserDto: AddRoleToUserDTO = new AddRoleToUserDTO(role.id);

        this.userService.addRoleToUser(user.id, addRoleToUserDto).subscribe((response: ApiResponse<UserDTO>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.refresh();
            this.showSuccessMessage('Role added successfully');
        });
    }

    protected removeRoleFromUser(user: UserDTO, role: RoleDTO): void {
        this.userService.removeRoleFromUser(user.id, role.id).subscribe((response: ApiResponse<UserDTO>) => {
            if (response.error) {
                this.showErrorMessage(response.error);
                return;
            }

            this.refresh();
            this.showSuccessMessage('Role removed successfully');
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

    protected showErrorMessage(error: ApiErrorDTO): void {
        this.notificationService.showErrorMessage(error.message);
    }

    protected showSuccessMessage(message: string): void {
        this.notificationService.showSuccessMessage(message);
    }

}