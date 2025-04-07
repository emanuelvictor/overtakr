package com.emanuelvictor.api.nonfunctional.authengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthEngineApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AuthEngineApplication.class, args);
    }

}
