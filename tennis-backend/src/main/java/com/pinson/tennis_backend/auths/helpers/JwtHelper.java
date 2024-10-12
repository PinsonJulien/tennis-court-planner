package com.pinson.tennis_backend.auths.helpers;

import jakarta.servlet.http.HttpServletRequest;

public class JwtHelper {
    public static String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
