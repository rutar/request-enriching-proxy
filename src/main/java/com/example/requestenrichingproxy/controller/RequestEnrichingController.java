package com.example.requestenrichingproxy.controller;

import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import com.example.requestenrichingproxy.service.RequestEnrichingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class RequestEnrichingController {

    private final RequestEnrichingService requestEnrichingService;

    public RequestEnrichingController(RequestEnrichingService requestEnrichingService) {
        this.requestEnrichingService = requestEnrichingService;
    }


    @GetMapping("/enriched-fields")
    public ResponseEntity<EnrichedDataForm> getFormFields(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String serviceName) throws SQLException {
        EnrichedDataForm response = requestEnrichingService.getEnrichedFormFields(firstName, lastName, serviceName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(@RequestBody EnrichedDataForm dataForm) throws JsonProcessingException {

        String result = requestEnrichingService.processFormSubmission(dataForm);
        return ResponseEntity.ok(result);
    }

}
