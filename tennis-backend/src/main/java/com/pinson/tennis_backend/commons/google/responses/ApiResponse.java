package com.pinson.tennis_backend.commons.google.responses;


import com.pinson.tennis_backend.commons.google.dtos.ApiErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// Api Response that follows the Google JSON Style Guide
// https://google.github.io/styleguide/jsoncstyleguide.xml
@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private String apiVersion = null;
    private String context = null;
    private String id = null;
    private String method = null;
    //private Object params = null;
    private T data = null;
    private ApiErrorDTO error = null;

    private ApiResponse() {}

    public static <T> ApiResponse<T> of(
        String apiVersion,
        String context,
        String id,
        String method,
        T data
    ) {
        return ApiResponse.<T>builder()
            .apiVersion(apiVersion)
            .context(context)
            .id(id)
            .method(method)
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> of(
        String apiVersion,
        String context,
        String id,
        String method,
        ApiErrorDTO error
    ) {
        return ApiResponse.<T>builder()
            .apiVersion(apiVersion)
            .context(context)
            .id(id)
            .method(method)
            .error(error)
            .build();
    }

}