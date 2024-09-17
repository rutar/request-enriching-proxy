package com.example.requestenrichingproxy.service;


import com.example.requestenrichingproxy.entity.AppUser;
import com.example.requestenrichingproxy.entity.EnrichedDataForm;
import com.example.requestenrichingproxy.entity.RequiredField;
import com.example.requestenrichingproxy.entity.ServiceDefinition;
import com.example.requestenrichingproxy.repository.ServiceDefinitionRepository;
import com.example.requestenrichingproxy.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestEnrichingService {

    private final UserRepository userRepository;

    private final ServiceDefinitionRepository serviceDefinitionRepository;

    private final JdbcTemplate jdbcTemplate;

    public RequestEnrichingService(UserRepository userRepository, ServiceDefinitionRepository serviceDefinitionRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.serviceDefinitionRepository = serviceDefinitionRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public EnrichedDataForm getEnrichedFormFields(String firstName, String lastName, String serviceName) throws SQLException {

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

            formValues.put(fieldName, fieldValue);
            formPresentations.put(fieldName, fieldPresentation);
        }

        return new EnrichedDataForm(formValues, formPresentations);
    }

    // Get user fields from AppUser entity using reflection
    private Map<String, String> getUserFields(AppUser user) {
        return Arrays.stream(user.getClass().getDeclaredFields())
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> {
                            field.setAccessible(true);
                            try {
                                Object value = field.get(user);
                                return value != null ? value.toString() : "";
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return "";
                            }
                        }
                ));
    }

    // Get user fields from database
    public Map<String, String> getUserFieldsFromDatabase(AppUser user) {
        List<String> fields = new ArrayList<>();

        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) LIKE UPPER(?) LIMIT 1";

        String appUserTableName = jdbcTemplate.queryForObject(sql, new Object[]{"%app_user%"}, String.class);

        // Query to get column names
        sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + appUserTableName + "'";
        jdbcTemplate.query(sql, rs -> {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                fields.add(columnName); // Initialize with null values
            }
        });

        // Fetch the specific user entity
        sql = "SELECT * FROM " + appUserTableName + " WHERE id = ?";

        Map<String, String> resultMap = new HashMap<>();

        jdbcTemplate.query(sql, new Object[]{user.getId()}, rs -> {
            for (String columnName : fields) {
                String columnValue = rs.getString(columnName);
                resultMap.put(toCamelCase(columnName), columnValue);
            }
        });

        return resultMap;
    }

    private String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder camelCaseString = new StringBuilder();
        boolean nextUpperCase = false;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    camelCaseString.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    camelCaseString.append(Character.toLowerCase(c));
                }
            }
        }

        return camelCaseString.toString();
    }
}
