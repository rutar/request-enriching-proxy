package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.client.DynamicFeignClient;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DynamicFeignClientServiceImpl implements DynamicFeignClientService {
    private final JacksonEncoder encoder;
    private final JacksonDecoder decoder;

    public DynamicFeignClientServiceImpl() {
        this.encoder = new JacksonEncoder();
        this.decoder = new JacksonDecoder();
    }

    @Override
    public Map<String, String> sendDynamicPostRequest(String url, Map<String, String> requestBody, Map<String, String> additionalHeaders) {
        DynamicFeignClient client = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .requestInterceptor(new CustomRequestInterceptor(additionalHeaders))
                .target(new Target<>() {
                    @Override
                    public Class<DynamicFeignClient> type() {
                        return DynamicFeignClient.class;
                    }

                    @Override
                    public String name() {
                        return "dynamicFeignClient";
                    }

                    @Override
                    public String url() {
                        return url;
                    }

                    @Override
                    public Request apply(RequestTemplate requestTemplate) {
                        return null;
                    }
                });

        return client.sendPostRequest(requestBody);
    }

    private record CustomRequestInterceptor(Map<String, String> additionalHeaders) implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate requestTemplate) {
            additionalHeaders.forEach(requestTemplate::header);
            requestTemplate.query("timestamp", String.valueOf(System.currentTimeMillis()));
        }
    }
}
