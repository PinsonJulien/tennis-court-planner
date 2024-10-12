package com.pinson.tennis_backend.auths.handlers;

import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.commons.responses.factories.IApiResponseFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private IApiResponseFactory apiResponseFactory;

    /*************************************************************************
     * AccessDeniedHandler implementation
     *************************************************************************/

    @Override
    public void handle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        final HttpStatus status = HttpStatus.FORBIDDEN;

        final BaseApiResponse<Exception> apiResponse = this.apiResponseFactory.createExceptionApiResponse(
            accessDeniedException,
            status,
            "auth.role-protected-route",
            "Auth"
        );

        this.apiResponseFactory.assignBaseApiResponseToHttpServletResponse(apiResponse, response);
    }
}
