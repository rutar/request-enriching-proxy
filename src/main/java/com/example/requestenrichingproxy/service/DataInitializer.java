package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.entity.ServiceDefinition;
import com.example.requestenrichingproxy.repository.ServiceDefinitionRepository;
import com.example.requestenrichingproxy.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "app.data.init", havingValue = "true")
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceDefinitionRepository serviceDefinitionRepository;


    @PostConstruct
    public void initData() {

        // Initialize Service Definitions
        ServiceDefinition service1 = ServiceDefinition.builder()
                .serviceName("service1")
                .serviceUrl("http://example.com/service1")
                .requiredFields("firstName, lastName, birthDate, birthPlace")
                .build();

        ServiceDefinition service2 = ServiceDefinition.builder()
                .serviceName("service2")
                .serviceUrl("http://anotherdomain.com/service2")
                .requiredFields("firstName, lastName, sex, currentAddress")
                .build();

        ServiceDefinition service3 = ServiceDefinition.builder()
                .serviceName("service3")
                .serviceUrl("https://secureddomain.com/service3")
                .requiredFields("lastName, birthDate, currentAddress, birthPlace, sex")
                .build();

        serviceDefinitionRepository.saveAll(List.of(service1, service2, service3));

        userRepository.save(AppUser.builder()
                .firstName("Jane")
                .lastName("Smith")
                .additionalAttributes("""
                        {
                          "birthPlace": "Quebec, Canada"
                        }""")
                .build());

        userRepository.save(AppUser.builder()
                .firstName("John")
                .lastName("Doe")
                .build());
    }
}
