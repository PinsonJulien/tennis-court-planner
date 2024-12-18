package com.pinson.tennis_backend.courts.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartialUpdateCourtDTO {
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    @Size(min = 1, max = 255, message = "Address must be between 1 and 255 characters")
    private String address;

    @Size(min = 1, max = 255, message = "Image URL must be between 1 and 255 characters")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    public boolean isNameSet() {
        return this.name != null;
    }

    public boolean isDescriptionSet() {
        return this.description != null;
    }

    public boolean isAddressSet() {
        return this.address != null;
    }

    public boolean isImageUrlSet() {
        return this.imageUrl != null;
    }
}
