package com.example.requestenrichingproxy.service;


import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import com.example.requestenrichingproxy.entity.RequiredField;
import com.example.requestenrichingproxy.entity.ServiceDefinition;
import com.example.requestenrichingproxy.repository.EnrichedDataFormRepository;
import com.example.requestenrichingproxy.repository.ServiceDefinitionRepository;
import com.example.requestenrichingproxy.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestEnrichingService {

    private final UserRepository userRepository;

    private final ServiceDefinitionRepository serviceDefinitionRepository;

    private final DynamicFeignClientService dynamicFeignClientService;

    private final EnrichedDataFormRepository enrichedDataFormRepository;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public RequestEnrichingService(UserRepository userRepository, ServiceDefinitionRepository serviceDefinitionRepository, DynamicFeignClientService dynamicFeignClientService, EnrichedDataFormRepository enrichedDataFormRepository) {
        this.userRepository = userRepository;
        this.serviceDefinitionRepository = serviceDefinitionRepository;
        this.dynamicFeignClientService = dynamicFeignClientService;
        this.enrichedDataFormRepository = enrichedDataFormRepository;
    }


    public EnrichedDataForm getEnrichedFormFields(String firstName, String lastName, String serviceName) {

        AppUser user = userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("User not found: " + firstName + " " + lastName));

        ServiceDefinition serviceDefinition = Optional.ofNullable(serviceDefinitionRepository.findByServiceName(serviceName))
                .orElseThrow(() -> new RuntimeException("Service not found: " + serviceName));

        Set<RequiredField> requiredFields = serviceDefinition.getRequiredFields();

        // Get user fields (from DB or reflection)
        Map<String, String> userFields = getUserFieldsFromDatabase(user);  // Or use reflection method getUserFields(AppUser user)

        // Prepare form fields and their presentations
        Map<String, String> formValues = new HashMap<>();
        Map<String, String> formPresentations = new HashMap<>();

        for (RequiredField requiredField : requiredFields) {
            String fieldName = requiredField.getFieldName();
            String fieldValue = userFields.getOrDefault(fieldName, null);
            String fieldPresentation = requiredField.getFieldPresentation();

            formPresentations.put(fieldName, fieldValue);
            formValues.put(fieldName, fieldPresentation);
        }

        return new EnrichedDataForm(formValues, formPresentations);
    }


    public Map<String, String> getUserFieldsFromDatabase(AppUser user) {
        Map<String, String> fieldValues = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            String className = toSnakeCase(AppUser.class.getSimpleName());

            // Query to get the table name
            String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) LIKE UPPER(?) LIMIT 1";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "%" + className + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String tableName = rs.getString("TABLE_NAME");

                        // Use DatabaseMetaData to retrieve column names
                        DatabaseMetaData metaData = connection.getMetaData();
                        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                            StringBuilder queryBuilder = new StringBuilder("SELECT ");
                            boolean first = true;
                            while (columns.next()) {
                                String columnName = columns.getString("COLUMN_NAME");
                                if (!first) {
                                    queryBuilder.append(", ");
                                }
                                queryBuilder.append(columnName);
                                first = false;
                            }
                            queryBuilder.append(" FROM ").append(tableName).append(" WHERE id = ?");

                            // Execute the query to get the user field values
                            try (PreparedStatement userStmt = connection.prepareStatement(queryBuilder.toString())) {
                                userStmt.setLong(1, user.getId());
                                try (ResultSet userRs = userStmt.executeQuery()) {
                                    if (userRs.next()) {
                                        ResultSetMetaData rsMetaData = userRs.getMetaData();
                                        int columnCount = rsMetaData.getColumnCount();
                                        for (int i = 1; i <= columnCount; i++) {
                                            String columnName = rsMetaData.getColumnName(i);
                                            String columnValue = userRs.getString(i);
                                            fieldValues.put(toCamelCase(columnName), columnValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fieldValues;
    }


    public String processFormSubmission(EnrichedDataForm formData) throws JsonProcessingException {

        String serviceName = formData.getServiceName();

        ServiceDefinition serviceDefinition = serviceDefinitionRepository.findByServiceName(serviceName);
        if (serviceDefinition == null) {
            throw new RuntimeException("Service not found: " + serviceName);
        }

        Set<String> requiredFields = serviceDefinition.getRequiredFields().stream()
                .map(RequiredField::getFieldName)
                .collect(Collectors.toSet());

        if (!formData.getValues().keySet().containsAll(requiredFields)) {
            throw new RuntimeException("Missing required fields for service: " + serviceName);
        }

        enrichedDataFormRepository.save(formData);


        String response = dynamicFeignClientService.sendDynamicPostRequest(serviceDefinition.getServiceUrl(), formData);
        System.out.println(response);
        return response;
    }


    // Utility function to convert snake_case to camelCase
    private String toCamelCase(String snakeCase) {
        String[] parts = snakeCase.toLowerCase().split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase())
                    .append(parts[i].substring(1));
        }
        return camelCaseString.toString();
    }

    // Utility function to convert CamelCase to snake_case
    private String toSnakeCase(String className) {
        return className.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
