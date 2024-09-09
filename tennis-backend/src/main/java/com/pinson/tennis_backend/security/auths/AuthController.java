package com.pinson.tennis_backend.security.auths;

import com.pinson.tennis_backend.security.jwts.JwtResponse;
import com.pinson.tennis_backend.security.jwts.JwtUtils;
import com.pinson.tennis_backend.security.users.CreateUserDTO;
import com.pinson.tennis_backend.security.users.IUserService;
import com.pinson.tennis_backend.security.users.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IUserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(
            this.authenticateUser(
                loginRequest.username(),
                loginRequest.password()
            )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
        @RequestBody SignupRequest signUpRequest
    ) {
        logger.debug("Signup request: {}", signUpRequest);
        final CreateUserDTO createUserDTO = new CreateUserDTO(
            signUpRequest.username(),
            signUpRequest.email(),
            this.passwordEncoder.encode(signUpRequest.password())
        );

        logger.debug("Create user DTO: {}", createUserDTO);

        this.userService.create(createUserDTO);

        return ResponseEntity.ok(
            this.authenticateUser(
                signUpRequest.username(),
                signUpRequest.password()
            )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final UserDTO userDTO = this.userService.findByUsername(userDetails.getUsername());

        return ResponseEntity.ok(
            userDTO
        );
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
