package com.pinson.tennis_backend.auths.services;

import com.pinson.tennis_backend.auths.dtos.AuthenticationDTO;
import com.pinson.tennis_backend.auths.dtos.LoginDTO;
import com.pinson.tennis_backend.auths.dtos.LogoutDTO;
import com.pinson.tennis_backend.auths.dtos.SignupDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;

public interface IAuthService {
    AuthenticationDTO login(LoginDTO loginDTO);
    AuthenticationDTO signup(SignupDTO signupDTO);
    void logout(LogoutDTO logoutDTO);

    UserDTO getCurrentUser();
}
