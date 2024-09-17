package com.example.requestenrichingproxy.repository;





import com.example.requestenrichingproxy.entity.ServiceDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDefinitionRepository extends JpaRepository<ServiceDefinition, Long> {
    ServiceDefinition findByServiceName(String serviceName);
}
