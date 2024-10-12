package com.pinson.tennis_backend.auths.controllers;

import com.pinson.tennis_backend.auths.dtos.AuthenticationDTO;
import com.pinson.tennis_backend.auths.dtos.LoginDTO;
import com.pinson.tennis_backend.auths.dtos.LogoutDTO;
import com.pinson.tennis_backend.auths.dtos.SignupDTO;
import com.pinson.tennis_backend.auths.dtos.requests.LoginRequest;
import com.pinson.tennis_backend.auths.dtos.requests.SignupRequest;
import com.pinson.tennis_backend.auths.helpers.JwtHelper;
import com.pinson.tennis_backend.auths.services.IAuthService;
import com.pinson.tennis_backend.commons.controllers.BaseController;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public BaseApiResponse<AuthenticationDTO> login(
        @Valid @RequestBody final LoginRequest loginRequest
    ) {
        final String method = "auth.login";

        try {
            final LoginDTO loginDTO = loginRequest.toLoginDTO();
            final AuthenticationDTO authenticationDTO = this.authService.login(loginDTO);

            return this.createResponse(
                HttpStatus.OK,
                method,
                authenticationDTO
            );
        } catch (Exception e) {
            return this.createExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                method,
                "Auth",
                e
            );
        }
    }

    @PostMapping("/signup")
    public BaseApiResponse<AuthenticationDTO> signup(
        @Valid @RequestBody final SignupRequest signupRequest
    ) {
        final String method = "auth.signup";

        try {
            final SignupDTO signupDTO = signupRequest.toSignupDTO();
            final AuthenticationDTO authenticationDTO = this.authService.signup(signupDTO);

            return this.createResponse(
                HttpStatus.CREATED,
                method,
                authenticationDTO
            );
        } catch (Exception e) {
            return this.createExceptionResponse(
                HttpStatus.BAD_REQUEST,
                method,
                "Auth",
                e
            );
        }
    }

    @PostMapping("/logout")
    public BaseApiResponse<Void> logout(
        final HttpServletRequest request
    ) {
        final String method = "auth.logout";

        try {
            final String token = JwtHelper.getTokenFromRequest(request);
            final LogoutDTO loginDTO = new LogoutDTO(token);
            this.authService.logout(loginDTO);

            return this.createResponse(
                HttpStatus.OK,
                method,
                null
            );
        } catch (Exception e) {
            return this.createExceptionResponse(
                HttpStatus.BAD_REQUEST,
                method,
                "Auth",
                e
            );
        }
    }


}
