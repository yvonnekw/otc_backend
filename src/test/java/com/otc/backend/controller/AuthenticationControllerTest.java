package com.otc.backend.controller;

import com.otc.backend.base.TestBase;
import com.otc.backend.body.RegistrationDtoGenerator;
import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.repository.RoleRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthenticationControllerTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);
/*
    @Container
    public DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File(System.getProperty("user.dir")+"/docker-compose.yml"))
                    .withLocalCompose(true);*/

    @Container

    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    RegistrationDtoGenerator registrationDtoGenerator;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void registerUserTest(){
        RegistrationDto registrationDto = registrationDtoGenerator.generateRandomRegistrationDto();
        ResponseEntity<String> response = registerUser(registrationDto, headers);
        String actualResults = response.getBody();
        logger.info("actual result " + actualResults);

       assertNotNull(actualResults);
       assertTrue(actualResults.contains("Georgina"));
    }

    @Test
    public void userLoginTest(){
        RegistrationDto registrationDto = registrationDtoGenerator.generateRandomRegistrationDto();
        ResponseEntity<String> response = registerUser(registrationDto, headers);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String registrationResponseBody = response.getBody();
        assertNotNull(registrationResponseBody);
        String username = extractUsernameFromRegistrationResponse(registrationResponseBody);
        logger.info("username extracted after registration  "+ username);
        logger.info("username before registration  "+ registrationResponseBody);

        String loginResponse = loginUserAndGetToken(username, registrationDto.getPassword());

        logger.info("actual result login " + loginResponse);

        assertNotNull(loginResponse);
    }
}