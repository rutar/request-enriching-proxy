package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.client.feignClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DynamicFeignClientService {

    public Map<String, String> sendDynamicPostRequest(String url, Map<String, String> requestBody){
        feignClient client = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(feignClient.class, url);

        // actually need to mock the service
        // return  client.sendPostRequest(requestBody);
       return requestBody;
    }
}