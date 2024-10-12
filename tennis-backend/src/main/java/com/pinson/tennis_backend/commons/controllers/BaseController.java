package com.pinson.tennis_backend.commons.controllers;

import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.commons.responses.factories.IApiResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


public abstract class BaseController {
    @Autowired
    protected IApiResponseFactory apiResponseFactory;

    protected <T> BaseApiResponse<T> createResponse(
        final HttpStatus status,
        final String method,
        final Object object
    ) {
        return this.apiResponseFactory.createApiResponse(
            object,
            status,
            method
        );
    }

    protected <T> BaseApiResponse<T> createExceptionResponse(
        HttpStatus status,
        String method,
        String domain,
        Exception exception
    ) {
        return this.apiResponseFactory.createExceptionApiResponse(
            exception,
            status,
            method,
            domain
        );
    }

}
