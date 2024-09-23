package com.example.requestenrichingproxy.client;

import feign.Headers;
import feign.RequestLine;

import java.util.Map;


public interface DynamicFeignClient {
    @RequestLine("POST /")
    @Headers("Content-Type: application/json")
    Map<String, String> sendPostRequest(Map<String, String> requestBody);
}