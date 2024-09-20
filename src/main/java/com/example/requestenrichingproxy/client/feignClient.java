package com.example.requestenrichingproxy.client;

import com.example.requestenrichingproxy.entity.AppUser;
import feign.Headers;
import feign.RequestLine;
import feign.Param;
import feign.Body;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "dynamicFeignClient", url = "http://example.com")
public interface feignClient {

    @RequestLine("POST /")
    @Headers("Content-Type: application/json")
    @Body("%7B\"key\":\"{value}\"%7D")
    String sendPostRequest(@Param("value") AppUser value);

}
