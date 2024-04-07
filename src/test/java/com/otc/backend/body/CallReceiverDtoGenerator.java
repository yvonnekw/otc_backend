package com.otc.backend.body;

import com.github.javafaker.Faker;
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

        //Optional<Users> userOptional = userRepository.findByUsername(username);

       // if (userOptional.isPresent()) {

         //   Users user = userOptional.get();
           // logger.info("user details with get username " + user);
           // return new CallReceiverDto(faker.phoneNumber().cellPhone(), user);

       // CallReceiverDto requestBody = String.format("{\"telephone\": \"%s\", \"username\": \"%s\"}", telephone, username);

        return new CallReceiverDto(telephone, new Users(username));

           // re
       // }
     //   else {
           // logger.error("User with username " + username + " not found");
            //return null;
        //}
    }

}
