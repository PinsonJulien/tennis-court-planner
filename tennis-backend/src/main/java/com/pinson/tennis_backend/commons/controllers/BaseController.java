package com.pinson.tennis_backend.commons.controllers;

import com.pinson.tennis_backend.commons.google.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public abstract class BaseController {
    private final String version;

    public BaseController(String version) {
        this.version = version;
    }

    protected String getVersion() {
        return version;
    }

    protected String generateId() {
        return UUID.randomUUID().toString();
    }

    protected String getContextFromRequest(HttpServletRequest request) {
        // returns parameter context from request
        return request.getParameter("context");
    }

    protected <T> ApiResponse<T> generateResponse(
        HttpServletRequest request,
        String id,
        String method,
        T data
    ) {
        return ApiResponse.of(
            this.getVersion(),
            this.getContextFromRequest(request),
            id,
            method,
            data
        );
    }







}
