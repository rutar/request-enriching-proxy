package com.example.requestenrichingproxy.config;

import com.example.requestenrichingproxy.service.DynamicFeignClientService;
import com.example.requestenrichingproxy.service.DynamicFeignClientServiceImpl;
import com.example.requestenrichingproxy.service.MockDynamicFeignClientService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FeignClientConfig {

    @Bean(name = "mockFeignClientServiceBean")
    @Primary
    @ConditionalOnProperty(name = "feign.client.mock", havingValue = "true")
    public DynamicFeignClientService mockDynamicFeignClientService() {
        return new MockDynamicFeignClientService();
    }

    @Bean
    @ConditionalOnProperty(name = "feign.client.mock", havingValue = "false", matchIfMissing = true)
    public DynamicFeignClientService realDynamicFeignClientService() {
        return new DynamicFeignClientServiceImpl();
    }
}