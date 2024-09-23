package com.otc.backend.controller;

import java.util.LinkedHashMap;
import java.util.List;

import com.otc.backend.exception.CallReceiverCreationException;
import com.otc.backend.models.CallReceiver;
import com.otc.backend.response.ApiResponse;
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
import com.otc.backend.models.Call;
//import com.otc.backend.publisher.RabbitMQJsonProducer;
import com.otc.backend.services.CallService;


@RestController
@RequestMapping("/calls")
@CrossOrigin("*")
public class CallController {

    private static final Logger logger = LoggerFactory.getLogger(CallController.class);
    private final CallService callService;

    // private final RabbitMQJsonProducer rabbitMQJsonProducer;
// RabbitMQJsonProducer rabbitMQJsonProducer

    public CallController(CallService callService) {
        this.callService = callService;
        //this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @GetMapping("{id}")
    public ResponseEntity<Call> getCallById(@PathVariable("id") long callId) {
        Call call = callService.getCallById(callId);
        return ResponseEntity.ok(call);
    }

    @GetMapping("/get-all-calls")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Call>> getAllCalls() {
        List<Call> calls = callService.getAllCalls();
        return ResponseEntity.ok(calls);
    }

    @PutMapping("{id}")
    public ResponseEntity<Call> updateCall(@PathVariable("id") Long callId, @RequestBody Call updatedCall) {
        Call call = callService.updateCall(callId, updatedCall);
        return ResponseEntity.ok(call);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCall(@PathVariable("id") Long callId) {
        callService.deleteCall(callId);
        return ResponseEntity.ok("Call deleted successfully.");
    }

    @GetMapping("/mycalls")
    public String calls() {
        return "my callls";
    }

    @PostMapping("/make-call")
    public ResponseEntity<ApiResponse<Call>> callsController(@RequestBody LinkedHashMap<String, String> body) {
        try {
            String userName = body.get("username");
            String startTime = body.get("startTime");
            String endTime = body.get("endTime");
            String discountForCalls = body.get("discountForCalls");
            String telephone = body.get("telephone");

            if (userName == null || startTime == null || endTime == null || discountForCalls == null || telephone == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Missing required fields."));
            }

            CallDto callsDto = new CallDto();
            callsDto.setStartTime(startTime);
            callsDto.setEndTime(endTime);
            callsDto.setDiscountForCalls(discountForCalls);

            Call call = callService.makeCall(userName, telephone, callsDto);

            try {
                // Send message to RabbitMQ
                // rabbitMQJsonProducer.sendJsonMessage(call);
                ApiResponse<Call> response = new ApiResponse<>(true, "Call created successfully.",
                        call);
                return ResponseEntity.ok(response);
            } catch (AmqpException e) {
                logger.error("Error sending message to RabbitMQ: " + e.getMessage());
                ApiResponse<Call> response = new ApiResponse<>(true,
                        "Call was created successfully, but failed to notify RabbitMQ.", call
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (CallReceiverCreationException e) {
            logger.error("Error creating call receiver: " + e.getMessage());
            ApiResponse<Call> response = new ApiResponse<>(false, "Call receiver could not be registered. The phone may already be registered.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Error creating new call: " + e.getMessage());
            ApiResponse<Call> response = new ApiResponse<>(false, "An internal error occurred. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping
    public ResponseEntity<List<Call>> getCallsByUsername(@RequestParam String username) {
        List<Call> calls = callService.getCallsByUsername(username);
        return ResponseEntity.ok(calls);
    }

    @GetMapping("/user/{username}/calls")
    public ResponseEntity<List<Call>> getCallsByUsernameAndStatus(
            @PathVariable String username,
            @RequestParam String status) {
        List<Call> calls = callService.getCallsByUsernameAndStatus(username, status);
        return ResponseEntity.ok(calls);
    }

}
