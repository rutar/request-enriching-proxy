package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.client.feignClient;
import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

@Service
public class DynamicFeignClientService {

    public String sendDynamicPostRequest(String url, EnrichedDataForm requestBody) {
        feignClient client = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(feignClient.class, url);

        return client.sendPostRequest(requestBody);
    }
}