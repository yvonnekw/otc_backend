package com.otc.backend.body;

import com.github.javafaker.Faker;
import com.github.javafaker.Friends;
import com.otc.backend.base.TestBase;
import com.otc.backend.dto.CallReceiverDto;
import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.models.Users;
import com.otc.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class CallReceiverDtoGenerator extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(CallReceiverDtoGenerator.class);
    private static final Faker faker = new Faker();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RegistrationDtoGenerator registrationDtoGenerator;

    HttpHeaders headers = new HttpHeaders();

    public CallReceiverDto generateCallReceiverDto() {
        RegistrationDto registrationDto = registrationDtoGenerator.generateRandomRegistrationDto();
        ResponseEntity<String> response = registerUser(registrationDto, headers);
        String actualResults = response.getBody();
        logger.info("actual result " + actualResults);
        String username = extractUsernameFromRegistrationResponse(actualResults);

        String loginResponse = loginUserAndGetToken(username, registrationDto.getPassword());
        headers.set("Authorization", "Bearer " + loginResponse);

        logger.info("actual result login " + loginResponse);

        String telephone = faker.phoneNumber().cellPhone();
        String fullName = faker.name().fullName();
        String relationship = faker.friends().toString();

        return new CallReceiverDto(telephone, username, fullName, relationship);
    }

}
