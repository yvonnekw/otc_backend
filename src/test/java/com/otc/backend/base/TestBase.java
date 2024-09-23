package com.otc.backend.base;

import com.github.javafaker.Faker;
import com.otc.backend.body.RegistrationDtoGenerator;

import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;

public class TestBase {

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    RegistrationDtoGenerator registrationDtoGenerator;
    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(TestBase.class);

    private static final Faker faker = new Faker();


    protected ResponseEntity<String> registerUser(RegistrationDto registrationDto, HttpHeaders headers) {
        HttpEntity<RegistrationDto> registrationEntity = new HttpEntity<>(registrationDto, headers);
        ResponseEntity<String> registrationResponse = restTemplate.exchange("/auth/register", HttpMethod.POST, registrationEntity, String.class);
        return registrationResponse;
    }

    protected String extractUsernameFromRegistrationResponse(String registrationResponseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(registrationResponseBody);

            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && !dataNode.isNull()) {

                JsonNode usernameNode = dataNode.get("username");
                if (usernameNode != null && !usernameNode.isNull()) {
                    return usernameNode.asText();
                } else {
                    System.out.println("Username field is missing or null in the response.");
                    return null;
                }
            } else {
                System.out.println("Data field is missing or null in the response.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String extractTokenFromLoginResponse(String loginResponseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(loginResponseBody);

            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && !dataNode.isNull()) {
                JsonNode tokenNode = dataNode.get("token");
                if (tokenNode != null && !tokenNode.isNull()) {
                    return tokenNode.asText();
                } else {
                    System.out.println("Token field is missing or null in the response.");
                    return null;
                }
            } else {
                System.out.println("Data field is missing or null in the response.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String loginUserAndGetToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LinkedHashMap<String, String> loginBody = new LinkedHashMap<>();
        loginBody.put("username", username);
        loginBody.put("password", password);

        HttpEntity<LinkedHashMap<String, String>> loginEntity = new HttpEntity<>(loginBody, headers);
        ResponseEntity<String> loginResponse = restTemplate.exchange("/auth/login", HttpMethod.POST, loginEntity, String.class);

        return extractTokenFromLoginResponse(loginResponse.getBody());
    }

    protected ResponseEntity<String> addCallReceiver() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegistrationDto registrationDto = registrationDtoGenerator.generateRandomRegistrationDto();
        ResponseEntity<String> response = registerUser(registrationDto, headers);
        String actualResults = response.getBody();
        logger.info("actual result " + actualResults);
        String username = extractUsernameFromRegistrationResponse(actualResults);

        String loginResponse = loginUserAndGetToken(username, registrationDto.getPassword());

        logger.info("actual result login " + loginResponse);

        String telephone = faker.phoneNumber().cellPhone();
        String fullName = faker.name().fullName();
        String relationship = faker.friends().toString();

        System.out.println("Full name " + fullName + "relationship " + relationship);

        String requestBody = "{\"telephone\": \"" + telephone +
                "\", \"Full Name\": \"" + fullName +
                "\", \"Relationship\": \"" + relationship +
                "\", \"username\": \"" + username + "\"}";


        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> calReceiverResponse = restTemplate.exchange("/call-receiver/add-receiver", HttpMethod.POST, requestEntity, String.class);

        logger.info("call receiver details " + calReceiverResponse);

        return calReceiverResponse;
    }

    protected String extractTelephoneNumber(String responseBody) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.get("telephone").asText();
        } catch (Exception e) {
            logger.error("Error extracting telephone number from response body", e);
            return null;
        }
    }

    protected String extractUsername(String responseBody) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.get("username").asText();
        } catch (Exception e) {
            logger.error("Error extracting username from response body", e);
            return null;
        }
    }

    protected String extractData(String responseBody) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.get("callId").asText();
        } catch (Exception e) {
            logger.error("Error extracting call id from response body", e);
            return null;
        }
    }

    protected String extractInvoiceId(String responseBody) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.get("invoiceId").asText();
        } catch (Exception e) {
            logger.error("Error extracting invoice id from response body", e);
            return null;
        }
    }

    protected String extractData(String responseBody, String fieldName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && !dataNode.isNull()) {
                JsonNode fieldNode = dataNode.get(fieldName);

                if (fieldNode != null && !fieldNode.isNull()) {
                    return fieldNode.asText();
                } else {
                    logger.warn("Field '{}' not found in 'data' node", fieldName);
                }
            } else {
                logger.warn("'data' field is null or not present in response body");
            }

        } catch (Exception e) {
            logger.error("Error extracting {} data from response body", fieldName, e);
        }

        return null;
    }

    protected String extract(String responseBody, String fieldName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && !dataNode.isNull()) {
                JsonNode userNode = dataNode.get("user");

                if (userNode != null && !userNode.isNull()) {
                    JsonNode fieldNode = userNode.get(fieldName);

                    if (fieldNode != null && !fieldNode.isNull()) {
                        return fieldNode.asText();
                    } else {
                        logger.warn("Field '{}' not found in 'data' node", fieldName);
                    }
                } else {
                    logger.warn("'data' field is null or not present in response body");
                }
            }

        } catch (Exception e) {
            logger.error("Error extracting {} data from response body", fieldName, e);
        }

        return null;
    }
}
