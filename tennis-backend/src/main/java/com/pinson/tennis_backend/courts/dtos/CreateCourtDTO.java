package com.pinson.tennis_backend.courts.dtos;

public record CreateCourtDTO(
    String name,
    String description,
    String location,
    String imageUrl
) {

}
