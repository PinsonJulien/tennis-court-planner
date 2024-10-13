package com.pinson.tennis_backend.commons.responses.factories;

import com.pinson.tennis_backend.commons.google.dtos.ErrorDTO;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Collection;

public interface IApiResponseFactory {

    <T> BaseApiResponse<T> createApiResponse(
        final Object object,
        final HttpStatus status,
        final String method
    );

    <T> BaseApiResponse<T> createApiResponse(
        final Object object,
        final HttpStatus status,
        final String method,
        final String params
    );

    <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final HttpStatus status,
        final String method,
        final String domain
    );

    <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final HttpStatus status,
        final String method,
        final String domain,
        final String params
    );

    <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final Collection<ErrorDTO> errors,
        final HttpStatus status,
        final String method,
        final String domain
    );

    <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final Collection<ErrorDTO> errors,
        final HttpStatus status,
        final String method,
        final String domain,
        final String params
    );

    void assignBaseApiResponseToHttpServletResponse(
        final BaseApiResponse<?> apiResponse,
        final HttpServletResponse response
    ) throws IOException;

}
