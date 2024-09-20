package com.example.requestenrichingproxy.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String additionalAttributes;

    // Method to get additional attributes
    public HashMap<String, String> getAdditionalAttributes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (this.additionalAttributes != null) {
                return objectMapper.readValue(this.additionalAttributes, new TypeReference<HashMap<String, String>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading additional attributes", e);
        }
        return new HashMap<>();
    }


    // Override toString() for better logging and debugging
    @Override
    public String toString() {
        return "ServiceResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", additionalAttributes=" + getAdditionalAttributes() +
                '}';
    }
}