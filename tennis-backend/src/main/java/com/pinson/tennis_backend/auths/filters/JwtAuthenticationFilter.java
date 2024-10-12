package com.pinson.tennis_backend.auths.filters;

import com.pinson.tennis_backend.auths.helpers.JwtHelper;
import com.pinson.tennis_backend.auths.providers.IJwtTokenProvider;
import com.pinson.tennis_backend.users.services.IUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private IJwtTokenProvider jwtTokenProvider;

    @Autowired
    private IUserDetailsService userDetailsService;


    /*************************************************************************
     * OncePerRequestFilter overrides
     *************************************************************************/

    @Override
    protected void doFilterInternal(
        @NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String token = JwtHelper.getTokenFromRequest(request);

            if (this.isTokenValid(token)) {
                final String username = this.jwtTokenProvider.getUsernameFromToken(token);
                final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                final SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
            }
        } catch (final Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    /*************************************************************************
     * Helper methods
     *************************************************************************/

    private boolean isTokenValid(String token) {
        return token != null && this.jwtTokenProvider.validateToken(token);
    }

}
