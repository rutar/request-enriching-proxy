package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.client.feignClient;
import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

@Service
public class DynamicFeignClientService {

    public String sendDynamicPostRequest(String url, EnrichedDataForm requestBody) throws JsonProcessingException {
        feignClient client = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(feignClient.class, url);

        ObjectMapper objectMapper = new ObjectMapper();

        // actually need to mock the service
        return objectMapper.writeValueAsString(requestBody.getValues());
      //return  client.sendPostRequest(requestBody);
    }
}