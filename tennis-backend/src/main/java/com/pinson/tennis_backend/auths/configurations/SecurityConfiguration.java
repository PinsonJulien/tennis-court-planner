package com.pinson.tennis_backend.auths.configurations;

import com.pinson.tennis_backend.auths.entry_points.JwtAuthenticationEntryPoint;
import com.pinson.tennis_backend.auths.filters.JwtAuthenticationFilter;
import com.pinson.tennis_backend.auths.handlers.CustomAccessDeniedHandler;
import com.pinson.tennis_backend.roles.enums.RoleEnum;
import com.pinson.tennis_backend.users.services.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private IUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
        final HttpSecurity http
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(this.corsConfigurationSource()))
            .sessionManagement(
                sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                    // Auth routes
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/api/auth/signup").permitAll()
                    // Swagger routes
                    .requestMatchers("/swagger-ui/index.html").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .requestMatchers("/webjars/**").permitAll()

                    // Role based access
                    /* TODO: In the future, we should rather use authorities instead of roles, as it is more flexible.
                        However, this will require a change in the database schema and the way we handle roles.
                        Roles will have many authorities, and users will have many roles, which will allow us to
                        have more granular control over the permissions of each user.
                     */

                    // Courts
                    .requestMatchers(HttpMethod.POST, "/api/courts").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.PATCH, "/api/courts/**").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.DELETE, "/api/courts/*").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )

                    // Users
                    .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole(
                        RoleEnum.SECRETARY.name(),
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.GET, "/api/users/*").hasAnyRole(
                        RoleEnum.SECRETARY.name(),
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.GET, "/api/users/deleted").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.POST, "/api/users/*/restore").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.POST, "/api/users/*/roles").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*/roles").hasAnyRole(
                        RoleEnum.ADMIN.name(),
                        RoleEnum.SUPER_ADMIN.name()
                    )

                    // All the rest of the routes do not require any specific role.

                    // Any other route requires authentication.
                    .anyRequest().authenticated()
            )
            .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(this.authenticationProvider())
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .exceptionHandling(
                exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(this.customAccessDeniedHandler)
            );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        final AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}
