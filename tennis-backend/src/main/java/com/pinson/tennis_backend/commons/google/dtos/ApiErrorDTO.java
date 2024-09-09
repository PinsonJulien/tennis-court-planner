package com.pinson.tennis_backend.commons.google.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorDTO {
    private String code = null;
    private String message = null;
    private List<ErrorDTO> errors = null;

    public static ApiErrorDTO of(String code, String message, List<ErrorDTO> errors) {
        return ApiErrorDTO.builder()
            .code(code)
            .message(message)
            .errors(errors)
            .build();
    }

    public static ApiErrorDTO of(String code, String message, ErrorDTO error) {
        return ApiErrorDTO.builder()
            .code(code)
            .message(message)
            .errors(List.of(error))
            .build();
    }
}
