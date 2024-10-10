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
    private String apiVersion;
    private String context;
    private String id;
    private String method;
    private Object params;
    private T data;
    private ApiErrorDTO error;
}