package com.otc.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.otc.backend.body.CallDtoGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.otc.backend.base.TestBase;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InvoiceControllerTest extends TestBase {

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void createInvoiceForOneCall() throws JSONException {
        CallDtoGenerator callDtoGenerator = new CallDtoGenerator();
        ResponseEntity<String> callReceiverResponse = addCallReceiver();
        assertEquals(HttpStatus.OK, callReceiverResponse.getStatusCode());

        String responseBody = callReceiverResponse.getBody();

        logger.info("call receiver response body " + responseBody);
        String telephone = extractData(responseBody, "telephone");
        String username = extractData(responseBody, "username");

        JSONObject requestBody = callDtoGenerator.makeCallDto(telephone, username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> makeCallResponse = restTemplate.exchange("/calls/make-call", HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.OK, makeCallResponse.getStatusCode());

        logger.info("Call body" + makeCallResponse.getBody());

        String callId = makeCallResponse.getBody();
        String extractedCallId = extractData(callId, "callId");

        JSONArray callIdsArray = new JSONArray();
        callIdsArray.put(extractedCallId);

        JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("callIds", callIdsArray);

        String requestBodyString = jsonRequestBody.toString();

        logger.info("Call id request body string " + requestBodyString);

        HttpEntity<String> invoiceRequestEntity = new HttpEntity<>(requestBodyString, headers);
        ResponseEntity<String> invoiceResponse = restTemplate.exchange("/invoices/create-invoice", HttpMethod.POST,
                invoiceRequestEntity, String.class);

        logger.info("invoice response " + invoiceResponse.getBody());

        assertEquals(HttpStatus.OK, invoiceResponse.getStatusCode());
    }

}
