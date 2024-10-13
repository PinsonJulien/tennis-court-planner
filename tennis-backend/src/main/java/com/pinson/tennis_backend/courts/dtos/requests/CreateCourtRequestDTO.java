package com.pinson.tennis_backend.courts.dtos.requests;

import com.pinson.tennis_backend.courts.dtos.CreateCourtDTO;

public record CreateCourtRequestDTO(
    String name,
    String description,
    String address,
    String imageUrl
) {
    public CreateCourtDTO toCreateCourtDTO() {
        return new CreateCourtDTO(name, description, address, imageUrl);
    }
}
