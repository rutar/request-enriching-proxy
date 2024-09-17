package com.example.requestenrichingproxy.entity;


import jakarta.persistence.*;
import lombok.*;

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
    private String birthPlace;
    private String sex;
}