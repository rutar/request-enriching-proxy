package com.example.requestenrichingproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.example.requestenrichingproxy.client")
@SpringBootApplication
public class RequestEnrichingProxy {
    public static void main(String[] args) {
        SpringApplication.run(RequestEnrichingProxy.class, args);
    }
}
