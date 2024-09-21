package com.otc.backend.controller;

import java.util.LinkedHashMap;

import com.otc.backend.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otc.backend.dto.LoginResponseDto;
import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.exception.EmailAlreadyTakenException;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Users;
//import com.otc.backend.publisher.RabbitMQJsonProducer;
import com.otc.backend.services.AuthenticationService;
import com.otc.backend.services.TokenService;
import com.otc.backend.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    //private final RabbitMQJsonProducer rabbitMQJsonProducer;
//, RabbitMQJsonProducer rabbitMQJsonProducer
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(UserService userService, TokenService tokenService,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        //this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @ExceptionHandler({ EmailAlreadyTakenException.class })
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<>("The email you provided is already taken", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ UserDoesNotExistException.class })
    public ResponseEntity<String> handleUserDoesNotExist() {
        return new ResponseEntity<>("The user you are looking for does not exist.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Users>> registerUser(@RequestBody RegistrationDto body) {
        try {
            Users newUser = userService.registerUser(body).getData();

            try {
                //rabbitMQJsonProducer.sendJsonMessage(body);
               ApiResponse<Users> response = new ApiResponse<>(true,
                        "User registered successfully.", newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (AmqpException e) {
               // logger.error("Error sending message to RabbitMQ: " + e.getMessage(), e);
                ApiResponse<Users> response = new ApiResponse<>(true,
                        "User registered successfully.", newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (EmailAlreadyTakenException e) {
            logger.error("Email already taken: " + e.getMessage(), e);
            ApiResponse<Users> response = new ApiResponse<>(false, "The email you provided is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            logger.error("Error occurred during user registration: " + e.getMessage(), e);
            ApiResponse<Users> response = new ApiResponse<>(false, "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginUser(@RequestBody LinkedHashMap<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            String token = tokenService.generateJwt(auth);
            Users user = userService.getUserByUsername(username);
            LoginResponseDto loginResponse = new LoginResponseDto(user, token);
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(true, "Login successful", loginResponse);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<LoginResponseDto> response = new ApiResponse<>(false, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PutMapping("/update/telephone")
    public ResponseEntity<ApiResponse<Users>> updateTelephoneNumber(@RequestBody LinkedHashMap<String, String> body) {
        try {
            String userName = body.get("username");
            String phone = body.get("mainTelephone");
            Users applicationUser = userService.getUserByUsername(userName);
            applicationUser.setTelephone(phone);
            Users updatedUser = userService.updateUser(applicationUser);
            ApiResponse<Users> response = new ApiResponse<>(true, "Telephone number updated successfully", updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserDoesNotExistException e) {
            ApiResponse<Users> response = new ApiResponse<>(false, "The user you are looking for does not exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            logger.error("Error updating telephone number for user: " + body.get("username"), e);
            ApiResponse<Users> response = new ApiResponse<>(false, "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/email/code")
    public ResponseEntity<ApiResponse<String>> createEmailVerification(
            @RequestBody LinkedHashMap<String, String> body) {
        try {
            userService.generateEmailVerification(body.get("username"));
            ApiResponse<String> response = new ApiResponse<>(true, "Verification code generated, email sent.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating email verification code for user: " + body.get("username"), e);
            ApiResponse<String> response = new ApiResponse<>(false, "Failed to generate email verification code.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApiResponse<Users>> updatePassword(@RequestBody LinkedHashMap<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            Users updatedUser = userService.setPassword(username, password);
            ApiResponse<Users> response = new ApiResponse<>(true, "Password updated successfully", updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserDoesNotExistException e) {
            ApiResponse<Users> response = new ApiResponse<>(false, "The user you are looking for does not exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            logger.error("Error updating password for user: " + body.get("username"), e);
            ApiResponse<Users> response = new ApiResponse<>(false, "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/username")
    public ResponseEntity<ApiResponse<String>> getUsername(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<String> response = new ApiResponse<>(false, "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = authentication.getName();
        ApiResponse<String> response = new ApiResponse<>(true, "Username retrieved successfully", username);
        return ResponseEntity.ok(response);
    }
}
