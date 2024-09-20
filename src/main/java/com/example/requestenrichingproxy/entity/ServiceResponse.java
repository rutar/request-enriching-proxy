package com.example.requestenrichingproxy.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ServiceResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceUrl;
    private String response;

    public ServiceResponse(String url, String responseString) {
        this.serviceUrl = url;
        this.response = responseString;
    }

}