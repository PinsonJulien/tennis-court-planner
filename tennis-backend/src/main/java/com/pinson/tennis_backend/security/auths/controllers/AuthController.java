package com.pinson.tennis_backend.security.auths.controllers;

import com.pinson.tennis_backend.commons.controllers.BaseController;
import com.pinson.tennis_backend.commons.responses.BaseApiResponse;
import com.pinson.tennis_backend.security.auths.dtos.requests.LoginRequest;
import com.pinson.tennis_backend.security.auths.dtos.requests.SignupRequest;
import com.pinson.tennis_backend.security.auths.services.IAuthService;
import com.pinson.tennis_backend.security.jwts.JwtResponse;
import com.pinson.tennis_backend.security.jwts.JwtUtils;
import com.pinson.tennis_backend.users.dtos.CreateUserDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import com.pinson.tennis_backend.users.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthService authService;

    @PostMapping("/signin")
    public BaseApiResponse<JwtResponse> signIn(
        @RequestBody LoginRequest loginRequest
    ) {
        final String method = "auth.signin";

        try {
            final JwtResponse jwtResponse = this.authenticateUser(
                loginRequest.username(),
                loginRequest.password()
            );

            return this.createResponse(
                HttpStatus.OK,
                method,
                jwtResponse
            );
        } catch (Exception e) {
            final HttpStatus status = HttpStatus.UNAUTHORIZED;

            return this.createExceptionResponse(
                status,
                method,
                "User",
                e
            );
        }
    }

    @PostMapping("/signup")
    public BaseApiResponse<JwtResponse> signUp(
        @RequestBody SignupRequest signUpRequest
    ) {
        final String method = "auth.signup";
        try {
            logger.debug("Signup request: {}", signUpRequest);
            final CreateUserDTO createUserDTO = new CreateUserDTO(
                signUpRequest.username(),
                signUpRequest.email(),
                this.passwordEncoder.encode(signUpRequest.password())
            );

            logger.debug("Create user DTO: {}", createUserDTO);

            this.userService.create(createUserDTO);
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.BAD_REQUEST;

            return this.createExceptionResponse(
                status,
                method,
                "User",
                e
            );
        }

        // Authenticate the user after signup
        final JwtResponse jwtResponse = this.authenticateUser(
            signUpRequest.username(),
            signUpRequest.password()
        );

        return this.createResponse(
            HttpStatus.CREATED,
            method,
            jwtResponse
        );
    }

    @GetMapping("/me")
    public BaseApiResponse<UserDTO> getAuthenticatedUser() {
        final String method = "auth.me";

        try {
            final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            final UserDTO userDTO = this.userService.findByUsername(userDetails.getUsername());

            return this.createResponse(
                HttpStatus.OK,
                method,
                userDTO
            );
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            final HttpStatus status = HttpStatus.NOT_FOUND;

            return this.createExceptionResponse(
                status,
                method,
                "User",
                e
            );
        }
    }

    private JwtResponse authenticateUser(String username, String password) {
        final Authentication authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                username,
                password
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = this.jwtUtils.generateJwtToken(authentication);

        return new JwtResponse(
            jwt,
            userDetails.getUsername()
        );
    }

}
