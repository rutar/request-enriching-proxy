package com.example.requestenrichingproxy.service;

import java.util.Map;

public interface DynamicFeignClientService {
    Map<String, String> sendDynamicPostRequest(String url, Map<String, String> requestBody, Map<String, String> additionalHeaders);
}