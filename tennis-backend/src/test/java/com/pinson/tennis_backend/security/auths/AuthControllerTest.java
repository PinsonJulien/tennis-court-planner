package com.pinson.tennis_backend.security.auths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinson.tennis_backend.security.auths.dtos.requests.LoginRequest;
import com.pinson.tennis_backend.security.auths.dtos.requests.SignupRequest;
import com.pinson.tennis_backend.security.jwts.JwtUtils;
import com.pinson.tennis_backend.security.users.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void authenticateUser() throws Exception {
        final LoginRequest loginRequest = new LoginRequest("testuser", "password");
        final Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(Mockito.any(Authentication.class))).thenReturn("jwtToken");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"jwtToken\",\"username\":\"testuser\"}"));
    }

    @Test
    void registerUser() throws Exception {
        final SignupRequest signupRequest = new SignupRequest("testuser", "test@test.com", "password");
        final Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(Mockito.any(Authentication.class))).thenReturn("jwtToken");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"jwtToken\",\"username\":\"testuser\"}"));
    }
}