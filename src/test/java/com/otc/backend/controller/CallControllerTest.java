package com.otc.backend.controller;

import com.otc.backend.base.TestBase;
import com.otc.backend.body.CallDtoGenerator;
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

    @Test
    public void makeCallTest() throws JSONException {
        CallDtoGenerator callDtoGenerator = new CallDtoGenerator();
        ResponseEntity<String> callReceiverResponse = addCallReceiver();
        assertEquals(HttpStatus.OK, callReceiverResponse.getStatusCode());

        String responseBody = callReceiverResponse.getBody();

        logger.info("call receiver response body " + responseBody);
        String telephone = extractData(responseBody, "telephone");
        logger.info("extracted telephone number " + telephone);
        String username = extract(responseBody, "username");
        logger.info("extracted username " + username);

        JSONObject requestBody = callDtoGenerator.makeCallDto(telephone, username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> makeCallResponse = restTemplate.exchange("/calls/make-call", HttpMethod.POST, requestEntity, String.class);

        assertEquals(HttpStatus.OK, makeCallResponse.getStatusCode());
    }
}