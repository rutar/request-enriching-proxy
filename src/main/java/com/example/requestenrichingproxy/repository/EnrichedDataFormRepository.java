package com.example.requestenrichingproxy.repository;

import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrichedDataFormRepository extends JpaRepository<EnrichedDataForm, Long> {
}