export class PartialUpdateCourtDTO {
    name?: string;
    description?: string;
    address?: string;
    imageUrl?: string;

    constructor(name: string, description: string, address: string, imageUrl: string) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
    }
}