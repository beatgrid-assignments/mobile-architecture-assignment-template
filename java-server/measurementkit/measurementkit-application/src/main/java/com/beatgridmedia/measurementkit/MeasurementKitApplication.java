package com.beatgridmedia.measurementkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class MeasurementKitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeasurementKitApplication.class, args);
    }
}
