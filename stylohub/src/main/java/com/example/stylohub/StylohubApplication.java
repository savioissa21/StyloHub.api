package com.example.stylohub;

import com.example.stylohub.infrastructure.config.properties.CloudinaryProperties;
import com.example.stylohub.infrastructure.config.properties.ResendProperties;
import com.example.stylohub.infrastructure.config.properties.StripeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CloudinaryProperties.class,
        StripeProperties.class,
        ResendProperties.class
})
public class StylohubApplication {

    public static void main(String[] args) {
        SpringApplication.run(StylohubApplication.class, args);
    }
}
