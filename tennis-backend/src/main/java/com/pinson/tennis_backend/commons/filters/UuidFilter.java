package com.pinson.tennis_backend.commons.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UuidFilter extends OncePerRequestFilter {
    public static final String UUID_REQUEST_ATTRIBUTE = "REQUEST_UUID";

    /*************************************************************************
     * OncePerRequestFilter overrides
     *************************************************************************/

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        // Each request will have a unique UUID
        final UUID uuid = java.util.UUID.randomUUID();
        request.setAttribute(UUID_REQUEST_ATTRIBUTE, uuid);

        // Continue with the request
        filterChain.doFilter(request, response);
    }

    /*************************************************************************
     * Helper methods
     *************************************************************************/

    public static UUID getUuidFromRequest(final HttpServletRequest request) {
        return (UUID) request.getAttribute(UUID_REQUEST_ATTRIBUTE);
    }

}
