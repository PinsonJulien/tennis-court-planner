import { UserDTO } from "../users/user.dto";
import { JwtDTO } from "./jwt.dto";

export class AuthenticationDTO {
    user: UserDTO | null = null;
    jwt: JwtDTO | null = null;

    constructor(data: Partial<AuthenticationDTO>) {
        Object.assign(this, data);
    }
}