package com.pinson.tennis_backend.courts.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pinson.tennis_backend.courts.dtos.PartialUpdateCourtDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartialUpdateCourtRequestDTO {
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    @Size(min = 1, max = 255, message = "Address must be between 1 and 255 characters")
    private String address;

    @Size(min = 1, max = 255, message = "Image URL must be between 1 and 255 characters")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    public PartialUpdateCourtDTO toPartialUpdateCourtDTO() {
        return new PartialUpdateCourtDTO(name, description, address, imageUrl);
    }
}
