package com.otc.backend.controller;

import com.otc.backend.models.Call;
import com.otc.backend.models.Users;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);
    /*
        //@Container
       // @ServiceConnection
        //static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16");
    /*
        @Container
        public DockerComposeContainer<?> environment =
                new DockerComposeContainer<>(new File(System.getProperty("user.dir")+"/docker-compose.yml"))
                        .withLocalCompose(true);*/
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldFindAllUsers() {
        //user/all-users
        Users[] user = restTemplate.getForObject("/user/all-users", Users[].class);
       // assertThat(user.length).isEqualTo(2);
        logger.info("user details " + Arrays.toString(user));
        assertThat(user.length).isNotNull();
        assertThat(Arrays.stream(user).findFirst()).isPresent();
    }

    @Test
    void shouldFindUserByUsername() {
       ResponseEntity<Users> response = restTemplate.exchange("/user/yodalpinky1", HttpMethod.GET,  null, Users.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).isNotNull();
    }

}