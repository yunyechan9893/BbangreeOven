package com.bbangle.bbangle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("http://localhost:3000", "http://landing.bbangle.store",
                        "http://127.0.0.1:5000", "http://www.bbangle.store", "http://localhost:63342")
                    .allowedHeaders("*")
                    .exposedHeaders("ACCESS_KEY", "Authorization", "RefreshToken")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
                    .allowCredentials(true);
            }
        };
    }

}
