package com.otc.backend.body;

import com.github.javafaker.Faker;
import com.otc.backend.base.TestBase;

import com.otc.backend.dto.CallReceiverDto;
import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.models.Role;
import com.otc.backend.models.Users;
import com.otc.backend.repository.RoleRepository;
import com.otc.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TestDataProvider extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestDataProvider.class);

    private static final Faker faker = new Faker();

    @Autowired
    private RoleRepository roleRepository;

   // @Autowired
    //TestDataProvider testDataProvider;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private UserRepository userRepository;
/*
    public RegistrationDto generateRandomRegistrationDto() {

        Optional<Role> optionalUserRole = roleRepository.findByAuthority("USER");
        Role userRole = optionalUserRole.orElseThrow(() -> new RuntimeException("USER role not found"));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        return new RegistrationDto(
                "Georgina",
                faker.name().lastName(),
                faker.internet().emailAddress(),
                "pwd",
                faker.phoneNumber().cellPhone(),
                authorities
        );

    }

    public CallReceiverDto generateCallReceiverDto() {

        RegistrationDto registrationDto = testDataProvider.generateRandomRegistrationDto();
        ResponseEntity<String> response = registerUser(registrationDto, headers);
        String actualResults = response.getBody();
        logger.info("actual result " + actualResults);
        String username = extractUsernameFromRegistrationResponse(actualResults);

        Optional<Users> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {

            Users user = userOptional.get();
            return new CallReceiverDto(faker.phoneNumber().cellPhone(), user);
        }
        else {
            logger.error("User with username " + username + " not found");
            return null;
        }

    }

    */
}
