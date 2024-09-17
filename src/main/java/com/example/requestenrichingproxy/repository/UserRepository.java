package com.example.requestenrichingproxy.repository;


import com.example.requestenrichingproxy.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    // Method to find users by both first and last name
    Optional<AppUser> findByFirstNameAndLastName(String firstName, String lastName);
}
