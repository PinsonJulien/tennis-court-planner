package com.pinson.tennis_backend.commons.handlers.controller_advices;

import com.pinson.tennis_backend.commons.google.dtos.ErrorDTO;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.commons.responses.factories.IApiResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationResponseEntityExceptionHandler {

    @Autowired
    private IApiResponseFactory apiResponseFactory;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseApiResponse<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException ex,
        final WebRequest request
    ) {
        final String method = "validation";
        final String domain = "validation";

        // Convert error message to a list of ErrorDTO.
        // The message is the field error message.
        // The reason is the field name.
        final List<ErrorDTO> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError -> ErrorDTO.builder()
                    .domain(domain)
                    .message(fieldError.getDefaultMessage())
                    .reason(fieldError.getField())
                    .build()
            )
            .toList();

        // set message to the exception message override the class
        final ExtendedMethodArgumentNotValidException extendedMethodArgumentNotValidException = new ExtendedMethodArgumentNotValidException(
            ex,
            "Validation error"
        );

        final BaseApiResponse<Object> response = this.apiResponseFactory.createExceptionApiResponse(
            extendedMethodArgumentNotValidException,
            errors,
            HttpStatus.BAD_REQUEST,
            method,
            domain
        );

        return response;
    }

    private static class ExtendedMethodArgumentNotValidException extends MethodArgumentNotValidException {
        private final String message;

        public ExtendedMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            String message
        ) {
            super(
                ex.getParameter(),
                ex.getBindingResult()
            );
            this.message = message;
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }
}
