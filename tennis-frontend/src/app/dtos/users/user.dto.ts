import { RoleDTO } from "../roles/role.dto";

export class UserDTO {
    id: string = ""; 
    username: string = "";
    email: string = "";
    createdAt: Date = new Date();
    updatedAt: Date = new Date();
    roles: RoleDTO[] = [];

    constructor(data: Partial<UserDTO>) {
        Object.assign(this, data);
    }
}