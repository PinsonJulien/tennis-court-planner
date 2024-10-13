package com.pinson.tennis_backend.commons.handlers.controller_advices;

import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.commons.responses.factories.IApiResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.LOWEST_PRECEDENCE)
public class UnhandledResponseEntityExceptionHandler {

    @Autowired
    private IApiResponseFactory apiResponseFactory;

    @ExceptionHandler(Exception.class)
    public BaseApiResponse<Object> handleUnhandledException(
        final Exception exception,
        final WebRequest request
    ) {
        final String method = "unhandled-exception";
        final String domain = "unhandled-exception";

        return this.apiResponseFactory.createExceptionApiResponse(
            exception,
            HttpStatus.I_AM_A_TEAPOT,
            method,
            domain
        );
    }

}
