package com.example.requestenrichingproxy.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MockDynamicFeignClientService implements DynamicFeignClientService {
    @Override
    public Map<String, String> sendDynamicPostRequest(String url, Map<String, String> requestBody, Map<String, String> additionalHeaders) {
        Map<String, String> response = new HashMap<>(requestBody);
        response.put("status", "mocked");
        return response;
    }
}
