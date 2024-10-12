package com.pinson.tennis_backend.commons.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApiConfiguration {
    @Value("${api.version}")
    private String version;
}
