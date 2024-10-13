package com.pinson.tennis_backend.commons.responses.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinson.tennis_backend.commons.configurations.ApiConfiguration;
import com.pinson.tennis_backend.commons.filters.UuidFilter;
import com.pinson.tennis_backend.commons.google.dtos.ApiErrorDTO;
import com.pinson.tennis_backend.commons.google.dtos.ErrorDTO;
import com.pinson.tennis_backend.commons.google.responses.ApiResponse;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class ApiResponseFactory implements IApiResponseFactory {
    private static final String CONTEXT_REQUEST_PARAMETER = "context";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ApiConfiguration apiConfiguration;

    /*************************************************************************
     * IApiResponseFactory implementation
     *************************************************************************/

    @Override
    public <T> BaseApiResponse<T> createApiResponse(
        final Object object,
        final HttpStatus status,
        final String method
    ) {
        return this.createApiResponse(
            object,
            status,
            method,
            null
        );
    }

    @Override
    public <T> BaseApiResponse<T> createApiResponse(
        final Object object,
        final HttpStatus status,
        final String method,
        final String params
    ) {
        return this.create(
            object,
            status,
            method,
            params
        );
    }

    @Override
    public <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final HttpStatus status,
        final String method,
        final String domain
    ) {
        return this.createExceptionApiResponse(
            exception,
            status,
            method,
            domain,
            null
        );
    }

    @Override
    public <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final HttpStatus status,
        final String method,
        final String domain,
        final String params
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

        return this.create(
            apiError,
            status,
            method,
            params
        );
    }

    @Override
    public <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final Collection<ErrorDTO> errors,
        final HttpStatus status,
        final String method,
        final String domain
    ) {
        return this.createExceptionApiResponse(
            exception,
            errors,
            status,
            method,
            domain,
            null
        );
    }

    @Override
    public <T> BaseApiResponse<T> createExceptionApiResponse(
        final Exception exception,
        final Collection<ErrorDTO> errors,
        final HttpStatus status,
        final String method,
        final String domain,
        final String params
    ) {
        final ApiErrorDTO apiError = ApiErrorDTO.builder()
            .code(String.valueOf(status.value()))
            .message(exception.getMessage())
            .errors(errors.stream().toList())
            .build();

        return this.create(
            apiError,
            status,
            method,
            params
        );
    }


    @Override
    public void assignBaseApiResponseToHttpServletResponse(
        final BaseApiResponse<?> apiResponse,
        final HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(apiResponse.getStatusCode().value());
        final String body = this.convertApiResponseToJson(apiResponse.getBody());

        response.getWriter().write(body);
    }

    /*************************************************************************
     * Helper methods
     *************************************************************************/

    protected String getContext() {
        return this.request.getParameter(CONTEXT_REQUEST_PARAMETER);
    }

    protected String getUUID() {
        // returns parameter uuid from request
        final UUID uuid = UuidFilter.getUuidFromRequest(this.request);
        return uuid.toString();
    }

    protected <T> BaseApiResponse<T> create(
        Object object,
        HttpStatus status,
        String method,
        String params
    ) {
        return new BaseApiResponse<T>(
            status,
            this.createApiResponse(
                object,
                method,
                params
            )
        );
    }

    protected <T> ApiResponse<T> createApiResponse(
        Object object,
        String method,
        String params
    ) {
        final ApiResponse.ApiResponseBuilder<T> builder = ApiResponse.builder();
        builder
            .apiVersion(this.apiConfiguration.getVersion())
            .context(this.getContext())
            .id(this.getUUID())
            .method(method)
            .params(params);

        if (object instanceof ApiErrorDTO)
            builder.error((ApiErrorDTO) object);
        else
            builder.data((T) object);

        return builder.build();
    }

    protected String convertApiResponseToJson(ApiResponse<?> apiResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(apiResponse);
    }

}
