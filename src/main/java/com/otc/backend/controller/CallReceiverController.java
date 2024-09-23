package com.otc.backend.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otc.backend.exception.CallReceiverCreationException;
import com.otc.backend.response.ApiResponse;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otc.backend.dto.CallDto;
import com.otc.backend.dto.CallReceiverDto;
import com.otc.backend.models.Call;
import com.otc.backend.models.CallReceiver;
import com.otc.backend.models.Users;
//import com.otc.backend.publisher.RabbitMQJsonProducer;
import com.otc.backend.services.CallReceiverService;
import com.otc.backend.services.CallService;
import com.otc.backend.services.UserService;

@RestController
@RequestMapping("/call-receiver")
@CrossOrigin("*")
public class CallReceiverController {

    private static final Logger logger = LoggerFactory.getLogger(CallReceiverController.class);

    //private final RabbitMQJsonProducer rabbitMQJsonProducer;
    private final CallReceiverService callReceiverService;

    //, RabbitMQJsonProducer rabbitMQJsonProducer
    public CallReceiverController(CallReceiverService callReceiverService) {
        this.callReceiverService = callReceiverService;
        //this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @GetMapping("/call")
    public String calls() {
        return "call receiver";
    }

    @GetMapping("/phone-numbers/username/{username}")
    public ResponseEntity<List<CallReceiver>> getDistinctPhoneNumbersForUser(@PathVariable String username) {
        List<CallReceiver> callReceivers = callReceiverService.getCallReceiversByUsername(username);
        return new ResponseEntity<>(callReceivers, HttpStatus.OK);
    }

    @PostMapping("/add-receiver")
    public ResponseEntity<ApiResponse<CallReceiver>> callReceiver(@RequestBody CallReceiverDto callReceiverDTO) {
        try {
            String telephone = callReceiverDTO.getTelephone();
            String username = callReceiverDTO.getUsername();

            logger.info("Received request to register call receiver for user: " + username);

            if (callReceiverService.isPhoneNumberRegisteredForUser(username, telephone)) {
                ApiResponse<CallReceiver> response = new ApiResponse<>(false,
                        "Phone number is already registered for the user. Please register another phone number."
                );
                return ResponseEntity.badRequest().body(response);
            }

            CallReceiver callReceiver = callReceiverService.addCallReceiver(username, callReceiverDTO);
            logger.info("Call receiver registered successfully: " + callReceiver);

            try {
                //rabbitMQJsonProducer.sendJsonMessage(callReceiver);
                ApiResponse<CallReceiver> response = new ApiResponse<>(true, "Phone number registered successfully.",
                        callReceiver);
                return ResponseEntity.ok(response);
            } catch (AmqpException e) {
                //logger.error("Error sending message to RabbitMQ: " + e.getMessage());
                e.printStackTrace();
                ApiResponse<CallReceiver> response = new ApiResponse<>(true,
                        "Phone number registered successfully, but failed to notify RabbitMQ.", callReceiver
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (CallReceiverCreationException e) {
            logger.error("Error creating call receiver: " + e.getMessage());
            e.printStackTrace();
            ApiResponse<CallReceiver> response = new ApiResponse<>(false, "Call Receiver can NOT be registered. The phone may already be registered.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Error registering new call receiver: " + e.getMessage());
            e.printStackTrace();
            ApiResponse<CallReceiver> response = new ApiResponse<>(false, "An internal error occurred.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/phone-numbers")
    public ResponseEntity<List<String>> findDistinctTelephoneByUserUsername(@RequestParam String username) {
        System.out.println("username " + username);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<String> phoneNumbers = callReceiverService.findDistinctTelephoneByUserUsername(username);

        System.out.println("phone numbers " + phoneNumbers);
        return ResponseEntity.ok(phoneNumbers);
    }

    @Transactional
    @GetMapping("/call-receivers")
    public ResponseEntity<List<CallReceiverDto>> getCallReceiversByUsername(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<CallReceiver> callReceivers = callReceiverService.getCallReceiversByUsername(username);

        // Convert CallReceiver objects to CallReceiverDTO objects containing only fullName and telephone
        List<CallReceiverDto> callReceiverDtos = callReceivers.stream()
                .map(callReceiver -> new CallReceiverDto(callReceiver.getFullName(), callReceiver.getTelephone()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(callReceiverDtos);
    }

    @Transactional
    @GetMapping("/get-all-receivers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CallReceiverDto>> getCallReceiversByUsername() {

        List<CallReceiver> callReceivers = callReceiverService.getAllCallReceivers();

        // Map CallReceiver entities to CallReceiverDto
        List<CallReceiverDto> callReceiverDtos = callReceivers.stream()
                .map(callReceiver -> {
                    // Convert CallReceiver to CallReceiverDto (adjust mapping logic as needed)
                    CallReceiverDto dto = new CallReceiverDto();
                    dto.setCallReceiverId(callReceiver.getCallReceiverId());
                    dto.setFullName(callReceiver.getFullName());
                    dto.setTelephone(callReceiver.getTelephone());
                    dto.setRelationship(callReceiver.getRelationship());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(callReceiverDtos);
    }
}
