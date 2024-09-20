package com.example.requestenrichingproxy.repository;


import com.example.requestenrichingproxy.entity.ServiceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceResponseRepository extends JpaRepository<ServiceResponse, Long> {
}

