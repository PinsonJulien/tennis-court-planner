package com.pinson.tennis_backend.commons.configurations;

import com.pinson.tennis_backend.commons.interceptors.UUIDInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UUIDInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UUIDInterceptor uuidInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.uuidInterceptor);
    }
}
