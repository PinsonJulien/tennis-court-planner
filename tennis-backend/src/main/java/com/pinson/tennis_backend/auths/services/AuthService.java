package com.pinson.tennis_backend.auths.services;

import com.pinson.tennis_backend.auths.dtos.*;
import com.pinson.tennis_backend.auths.providers.IJwtTokenProvider;
import com.pinson.tennis_backend.users.dtos.CreateUserDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import com.pinson.tennis_backend.users.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IJwtTokenProvider jwtTokenProvider;

    @Autowired
    private IUserService userService;

    /*************************************************************************
     * IAuthService implementation
     *************************************************************************/

    @Override
    public AuthenticationDTO login(LoginDTO loginDTO) {
        final JwtDTO jwtDTO = this.authenticate(loginDTO.username(), loginDTO.password());
        final UserDTO userDTO = this.userService.findByUsername(loginDTO.username());

        return new AuthenticationDTO(userDTO, jwtDTO);
    }

    @Override
    public AuthenticationDTO signup(SignupDTO signupDTO) {
        final CreateUserDTO createUserDTO = signupDTO.toCreateUserDTO();
        final UserDTO userDTO = this.userService.create(createUserDTO);
        final JwtDTO jwtDTO = this.authenticate(userDTO.username(), signupDTO.password());

        return new AuthenticationDTO(userDTO, jwtDTO);
    }

    @Override
    public void logout(LogoutDTO logoutDTO) {
        this.jwtTokenProvider.blacklistToken(logoutDTO.token());
    }

    /*************************************************************************
     * Helper methods
     *************************************************************************/

    protected JwtDTO authenticate(final String username, final String password) {
        Authentication authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                username,
                password
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return this.jwtTokenProvider.generateToken(authentication);
    }
}
