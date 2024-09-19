package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.entity.RequiredField;
import com.example.requestenrichingproxy.entity.ServiceDefinition;
import com.example.requestenrichingproxy.repository.ServiceDefinitionRepository;
import com.example.requestenrichingproxy.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@ConditionalOnProperty(name = "app.data.init", havingValue = "true")
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceDefinitionRepository serviceDefinitionRepository;


    @PostConstruct
    public void initData() {

        // Initialize Required Fields
        RequiredField firstName = RequiredField.builder()
                .fieldName("firstName")
                .fieldPresentation("First Name:")
                .build();

        RequiredField lastName = RequiredField.builder()
                .fieldName("lastName")
                .fieldPresentation("Last Name:")
                .build();

        RequiredField birthDate = RequiredField.builder()
                .fieldName("birthDate")
                .fieldPresentation("Date Of Birth:")
                .build();

        RequiredField birthPlace = RequiredField.builder()
                .fieldName("birthPlace")
                .fieldPresentation("Place Of Birth:")
                .build();

        RequiredField sex = RequiredField.builder()
                .fieldName("sex")
                .fieldPresentation("Sex:")
                .build();

        RequiredField currentAddress = RequiredField.builder()
                .fieldName("currentAddress")
                .fieldPresentation("Current Address:")
                .build();


        // Initialize Service Definitions
        ServiceDefinition service1 = ServiceDefinition.builder()
                .serviceName("service1")
                .serviceUrl("http://example.com/service1")
                .requiredFields(Set.of(firstName, lastName, birthDate, birthPlace))
                .build();

        ServiceDefinition service2 = ServiceDefinition.builder()
                .serviceName("service2")
                .serviceUrl("http://example.com/service2")
                .requiredFields(Set.of(firstName, lastName, sex, currentAddress))
                .build();

        ServiceDefinition service3 = ServiceDefinition.builder()
                .serviceName("service3")
                .serviceUrl("http://example.com/service3")
                .requiredFields(Set.of(lastName, birthDate, currentAddress, birthPlace, sex))
                .build();

        serviceDefinitionRepository.saveAll(List.of(service1, service2, service3));

        userRepository.save(AppUser.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthPlace("USA, Ohio")
                .sex("Female")
                .build());

        userRepository.save(AppUser.builder()
                .firstName("John")
                .lastName("Doe")
                .sex("Male")
                .build());
    }
}
