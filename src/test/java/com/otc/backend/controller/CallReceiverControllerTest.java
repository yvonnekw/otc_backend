package com.otc.backend.controller;

import com.otc.backend.base.TestBase;
import com.otc.backend.body.CallReceiverDtoGenerator;
import com.otc.backend.body.RegistrationDtoGenerator;
import com.otc.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.github.javafaker.Faker;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CallReceiverControllerTest extends TestBase {

    @Container
    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16");
    @Test

    public void addCallReceiverTest(){
        ResponseEntity<String> callReceiverResponse = addCallReceiver();
        assertEquals(HttpStatus.OK, callReceiverResponse.getStatusCode());
    }



}