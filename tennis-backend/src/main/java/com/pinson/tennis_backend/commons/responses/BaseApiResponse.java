package com.pinson.tennis_backend.commons.responses;

import com.pinson.tennis_backend.commons.google.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseApiResponse<T> extends ResponseEntity<ApiResponse<T>> {
    public BaseApiResponse(
        final HttpStatus status,
        final ApiResponse<T> body
    ) {
        super(
            body,
            status
        );
    }
}
