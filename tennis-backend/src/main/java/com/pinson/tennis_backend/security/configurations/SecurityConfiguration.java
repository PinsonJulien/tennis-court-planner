package com.pinson.tennis_backend.security.configurations;

import com.pinson.tennis_backend.security.jwts.JwtAuthenticationEntryPoint;
import com.pinson.tennis_backend.security.jwts.JwtRequestFilter;
import com.pinson.tennis_backend.security.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true
)
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private IUserService userService;

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(this.userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        final HttpSecurity http,
        final HandlerMappingIntrospector introspector
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                (req) -> {
                    req.requestMatchers(
                        "/**",
                        "/api/v1/auth/**"
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated();
                }
            )
            .exceptionHandling(
                (exception) -> {
                    exception.authenticationEntryPoint(this.jwtAuthenticationEntryPoint);
                }
            )
            .sessionManagement(
                (session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }
            )
            .authenticationProvider(this.authenticationProvider())
            .addFilterBefore(
                this.jwtRequestFilter(),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
