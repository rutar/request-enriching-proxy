package com.example.requestenrichingproxy.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ServiceDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String serviceUrl;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "service_definition_required_field",
            joinColumns = @JoinColumn(name = "service_definition_id"),
            inverseJoinColumns = @JoinColumn(name = "required_field_id")
    )
    private Set<RequiredField> requiredFields;

}