export class JwtDTO {
    token: string = '';
    type: string = '';
    issuedAt: Date = new Date();
    expiration: Date = new Date();
  
    constructor(data: Partial<JwtDTO>) {
        Object.assign(this, data);
    }
}