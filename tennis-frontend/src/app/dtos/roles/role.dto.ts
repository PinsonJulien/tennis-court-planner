export class RoleDTO {
    id: number = 0;
    name: string = "";

    constructor(data: Partial<RoleDTO>) {
        Object.assign(this, data);
    }
}