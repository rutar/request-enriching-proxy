package com.example.enrichedrequestproxy.service;


import com.example.enrichedrequestproxy.repository.UserRepository;


public class EnrichedRequestService {

    private final UserRepository userRepository;



    public EnrichedRequestService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}