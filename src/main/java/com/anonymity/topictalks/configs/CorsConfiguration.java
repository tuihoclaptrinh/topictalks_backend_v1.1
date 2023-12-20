package com.anonymity.topictalks.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configs
 * - Created At: 25-09-2023 18:03:18
 * @since 1.0 - version of class
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://topictalks.online", "https://topictalksbackendv11-production.up.railway.app:5000")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

}
