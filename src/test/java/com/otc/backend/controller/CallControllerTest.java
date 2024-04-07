package com.otc.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otc.backend.base.TestBase;
import com.otc.backend.body.RegistrationDtoGenerator;
import com.otc.backend.repository.RoleRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CallControllerTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);

    @Autowired
    TestRestTemplate restTemplate;

   // @Autowired
   // RegistrationDtoGenerator registrationDtoGenerator;

    //@Autowired
    //private RoleRepository roleRepository;

    @Test
    public void makeCallTest() throws JSONException {
        ResponseEntity<String> callReceiverResponse = addCallReceiver();
        assertEquals(HttpStatus.OK, callReceiverResponse.getStatusCode());

        String responseBody = callReceiverResponse.getBody();

        logger.info("call receiver response body " + responseBody);
        String telephone = extractTelephoneNumber(responseBody);
        String username = extractUsername(responseBody);

        JSONObject requestBody = new JSONObject();
        requestBody.put("startTime", "10:13:45");
        requestBody.put("endTime", "14:20:05");
        requestBody.put("discountForCalls", 10);
        requestBody.put("username", username);
        requestBody.put("telephone", telephone);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> makeCallResponse = restTemplate.exchange("/calls/make-call", HttpMethod.POST, requestEntity, String.class);

        assertEquals(HttpStatus.OK, makeCallResponse.getStatusCode());
    }

    private String extractTelephoneNumber(String responseBody) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.get("telephone").asText();
        } catch (Exception e) {
            logger.error("Error extracting telephone number from response body", e);
            return null;
        }
    }

    private String extractUsername(String responseBody) {


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.get("username").asText();
        } catch (Exception e) {
            logger.error("Error extracting username from response body", e);
            return null;
        }
    }


}