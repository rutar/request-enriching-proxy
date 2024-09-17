package com.example.requestenrichingproxy.service;


import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import com.example.requestenrichingproxy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestEnrichingService {

    private final UserRepository userRepository;



    public RequestEnrichingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public EnrichedDataForm getEnrichedFormFields(String firstName, String lastName) {

        AppUser user = userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("User not found: " + firstName + " " + lastName));

        return new EnrichedDataForm(
                Map.of( "firstName","First Name:",  "lastName","Last Name:"),
                Map.of( "firstName",user.getFirstName(),  "lastName",user.getLastName()));

         // TODO: Current implementation is not complete, implement final service method
    }
}