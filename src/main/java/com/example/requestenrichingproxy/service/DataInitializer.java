package com.example.requestenrichingproxy.service;

import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.data.init", havingValue = "true")
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;


    @PostConstruct
    public void initData() {

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

        userRepository.save(AppUser.builder()
                .firstName("Jaana")
                .lastName("Sepp")
                .sex("Female")
                .build());

    }
}
