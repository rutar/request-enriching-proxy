package com.example.requestenrichingproxy.service;


import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.entity.ServiceDefinition;
import com.example.requestenrichingproxy.entity.ServiceResponse;
import com.example.requestenrichingproxy.repository.ServiceDefinitionRepository;
import com.example.requestenrichingproxy.repository.ServiceResponseRepository;
import com.example.requestenrichingproxy.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestEnrichingService {

    private final UserRepository userRepository;

    private final ServiceDefinitionRepository serviceDefinitionRepository;

    private final DynamicFeignClientService dynamicFeignClientService;

    private final ServiceResponseRepository serviceResponseRepository;

    public RequestEnrichingService(UserRepository userRepository,
                                   ServiceDefinitionRepository serviceDefinitionRepository,
                                   DynamicFeignClientService dynamicFeignClientService,
                                   ServiceResponseRepository serviceResponseRepository) {
        this.userRepository = userRepository;
        this.serviceDefinitionRepository = serviceDefinitionRepository;
        this.dynamicFeignClientService = dynamicFeignClientService;
        this.serviceResponseRepository = serviceResponseRepository;
    }


    public Map<String, String> getEnrichedFormFields(String firstName, String lastName, String serviceName) {

        AppUser user = userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("User not found: " + firstName + " " + lastName));

        ServiceDefinition serviceDefinition = Optional.ofNullable(serviceDefinitionRepository.findByServiceName(serviceName))
                .orElseThrow(() -> new RuntimeException("Service not found: " + serviceName));

        Set<String> requiredFields = Arrays.stream(serviceDefinition.getRequiredFields().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        // Prepare form fields and their presentations
        Map<String, String> formValues = new HashMap<>();

        for (String requiredField : requiredFields) {
            if (requiredField.equals("firstName")) {
                formValues.put(requiredField, firstName);
                continue;
            }
            if (requiredField.equals("lastName")) {
                formValues.put(requiredField, lastName);
                continue;
            }
            String fieldValue = user.getAdditionalAttributes().getOrDefault(requiredField, null);
            formValues.put(requiredField, fieldValue);
        }

        return formValues;
    }

    public Map<String, String> processFormSubmission(String serviceName, Map<String, String> userFormData) {

        ServiceDefinition serviceDefinition = serviceDefinitionRepository.findByServiceName(serviceName);
        if (serviceDefinition == null) {
            throw new RuntimeException("Service not found: " + serviceName);
        }

        Set<String> requiredFields = Arrays.stream(serviceDefinition.getRequiredFields().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        if (!userFormData.keySet().containsAll(requiredFields)) {
            throw new RuntimeException("Missing required fields for service: " + serviceName);
        }

        Map<String, String> response = dynamicFeignClientService.sendDynamicPostRequest(serviceDefinition.getServiceUrl(), userFormData);

        String responseJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            responseJson = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            responseJson = "Error converting response to JSON";
        }

        // Create and save new ServiceResponse entity
        serviceResponseRepository.save(new ServiceResponse(serviceDefinition.getServiceUrl(), responseJson));

        System.out.println(response);
        return response;
    }
}
