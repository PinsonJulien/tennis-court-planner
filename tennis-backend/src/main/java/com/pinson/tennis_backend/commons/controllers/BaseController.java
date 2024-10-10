package com.pinson.tennis_backend.commons.controllers;

import com.pinson.tennis_backend.commons.google.dtos.ApiErrorDTO;
import com.pinson.tennis_backend.commons.google.dtos.ErrorDTO;
import com.pinson.tennis_backend.commons.interceptors.UUIDInterceptor;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.commons.responses.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

public abstract class BaseController {
    @Autowired
    private ResponseFactory responseFactory;

    public BaseController() {
        //
    }

    protected String getUUID() {
        return UUIDInterceptor.getUUID();
    }

    protected <T> BaseApiResponse<T> createResponse(
        HttpStatus status,
        String method,
        Object object
    ) {
        return this.responseFactory.create(
            object,
            status,
            this.getUUID(),
            method,
            null
        );
    }

    protected <T> BaseApiResponse<T> createExceptionResponse(
        HttpStatus status,
        String method,
        String domain,
        Exception exception
    ) {
        final ErrorDTO error = ErrorDTO.builder()
            .domain(domain)
            .reason(exception.getClass().getSimpleName())
            .message(exception.getMessage())
            .build();

        final ApiErrorDTO apiError = ApiErrorDTO.builder()
            .code(String.valueOf(status.value()))
            .message(error.getMessage())
            .errors(List.of(error))
            .build();

        return this.createResponse(
            status,
            method,
            apiError
        );
    }

}
