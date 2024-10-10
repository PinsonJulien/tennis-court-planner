package com.pinson.tennis_backend.commons.responses;

import com.pinson.tennis_backend.commons.google.dtos.ApiErrorDTO;
import com.pinson.tennis_backend.commons.google.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {
    @Autowired
    private HttpServletRequest request;

    @Value("${api.version}")
    private String apiVersion;

    public ResponseFactory() {
        //
    }

    public <T> BaseApiResponse<T> create(
        Object object,
        HttpStatus status,
        String id,
        String method,
        String params
    ) {
        return new BaseApiResponse<T>(
            status,
            this.createApiResponse(
                object,
                id,
                method,
                params
            )
        );
    }


    /*public <T> ResponseEntity<ApiResponse<T>> createResponseEntity(
        Object object,
        HttpStatus status,
        String apiVersion,
        String id,
        String method,
        String params
    ) {
        return ResponseEntity
            .status(status)
            .body(
                this.createApiResponse(
                    object,
                    apiVersion,
                    id,
                    method,
                    params
                )
            );
    }*/

    protected <T> ApiResponse<T> createApiResponse(
        Object object,
        String id,
        String method,
        String params
    ) {
        final ApiResponse.ApiResponseBuilder<T> builder = ApiResponse.builder();
        builder
            .apiVersion(this.apiVersion)
            .context(this.getContext())
            .id(id)
            .method(method)
            .params(params);

        if (object instanceof ApiErrorDTO)
            builder.error((ApiErrorDTO) object);
        else
            builder.data((T) object);

        return builder.build();
    }

    protected String getContext() {
        // returns parameter context from request
        return this.request.getParameter("context");
    }

}
