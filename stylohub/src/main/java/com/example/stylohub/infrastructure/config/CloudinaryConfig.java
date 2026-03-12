package com.example.stylohub.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.example.stylohub.infrastructure.config.properties.CloudinaryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary cloudinary(CloudinaryProperties props) {
        return new Cloudinary(Map.of(
                "cloud_name", props.cloudName(),
                "api_key",    props.apiKey(),
                "api_secret", props.apiSecret(),
                "secure",     true
        ));
    }
}
