package com.example.requestenrichingproxy.controller;

import com.example.requestenrichingproxy.service.RequestEnrichingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RequestEnrichingController {

    private final RequestEnrichingService requestEnrichingService;

    public RequestEnrichingController(RequestEnrichingService requestEnrichingService) {
        this.requestEnrichingService = requestEnrichingService;
    }


    @GetMapping("/enriched-fields")
    public ResponseEntity<Map<String, String>> getFormFields(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String serviceName) {
        Map<String, String> response = requestEnrichingService.getEnrichedFormFields(firstName, lastName, serviceName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitForm(@RequestParam String serviceName, @RequestBody Map<String, String> dataForm) {
        Map<String, String> result = requestEnrichingService.processFormSubmission(serviceName, dataForm);
        return ResponseEntity.ok(result);
    }

}
