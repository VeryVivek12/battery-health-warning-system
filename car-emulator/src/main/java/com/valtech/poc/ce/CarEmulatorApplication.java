package com.valtech.poc.ce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarEmulatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarEmulatorApplication.class, args);
    }
}