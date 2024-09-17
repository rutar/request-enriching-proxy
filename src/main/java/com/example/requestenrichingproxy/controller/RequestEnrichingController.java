package com.example.requestenrichingproxy.controller;

import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import com.example.requestenrichingproxy.service.RequestEnrichingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam String lastName) {
        EnrichedDataForm response = requestEnrichingService.getEnrichedFormFields(firstName, lastName);
        return ResponseEntity.ok(response);
    }

}
