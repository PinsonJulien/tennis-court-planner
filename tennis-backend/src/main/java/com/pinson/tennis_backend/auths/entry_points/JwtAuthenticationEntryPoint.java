package com.pinson.tennis_backend.auths.entry_points;

import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.commons.responses.factories.IApiResponseFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private IApiResponseFactory apiResponseFactory;

    /*************************************************************************
     * AuthenticationEntryPoint implementation
     *************************************************************************/

    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException
    ) throws IOException, ServletException {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;

        final BaseApiResponse<Exception> apiResponse = this.apiResponseFactory.createExceptionApiResponse(
            authException,
            status,
            "auth.protected-route",
            "Auth"
        );

        this.apiResponseFactory.assignBaseApiResponseToHttpServletResponse(apiResponse, response);
    }
}
