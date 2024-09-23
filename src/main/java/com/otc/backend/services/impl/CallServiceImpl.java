package com.otc.backend.services.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.otc.backend.services.CallService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.otc.backend.dto.CallDto;
import com.otc.backend.exception.CallCreationException;
import com.otc.backend.exception.CallReceiverNotFoundException;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Call;
import com.otc.backend.models.CallReceiver;

import com.otc.backend.models.Users;
//import com.otc.backend.publisher.RabbitMQJsonProducer;
//import com.otc.backend.publisher.RabbitMQProducer;
import com.otc.backend.repository.CallReceiverRepository;
import com.otc.backend.repository.CallRepository;
//import opticaltelephonecompany.otc.repository.CurrentCallRepository;
import com.otc.backend.repository.UserRepository;

@Service
public class CallServiceImpl implements CallService {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);
    BigDecimal netCost;
    private final CallRepository callRepository;
    private final UserRepository userRepository;
    @Autowired
    private final CallReceiverRepository callReceiverRepository;
    // RabbitMQJsonProducer rabbitMQJsonProducer

    public CallServiceImpl(CallRepository callsRepository, UserRepository userRepository,
                           CallReceiverRepository callReceiverRepository
    ) {
        this.callRepository = callsRepository;
        this.userRepository = userRepository;
        this.callReceiverRepository = callReceiverRepository;
    }

    public Call getCallById(Long callId) {
        return callRepository.findById(callId).orElseThrow(UserDoesNotExistException::new);
    }

    public List<Call> getAllCalls() {
        return callRepository.findAll();
    }

    public Call updateCall(Long callId, Call updatedCall) {

        Call call = callRepository.findById(callId).orElseThrow(
                () -> new ResourceNotFoundException("Call not found with the given Id : " + callId));
        call.setStartTime(updatedCall.getStartTime());
        call.setEndTime(updatedCall.getEndTime());

        Call updatedCallObj = callRepository.save(call);
        return updatedCallObj;
    }

    public void deleteCall(Long callId) {
        Users user = userRepository.findById(callId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with the given Id : " + callId));

        callRepository.deleteById(callId);
    }

    @Transactional
    public Call makeCall(String username, String telephone, CallDto callsDTO) {
        try {
            Call call = new Call();
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserDoesNotExistException());

            CallReceiver callReceiver = callReceiverRepository.findByTelephone(telephone)
                    .orElseThrow(() -> new CallReceiverNotFoundException());

            LocalTime startTime = LocalTime.parse(callsDTO.getStartTime());
            LocalTime endTime = LocalTime.parse(callsDTO.getEndTime());

            long durationSeconds = call.calculateDurationInSeconds(startTime, endTime);
            System.out.println("Duration in seconds: " + durationSeconds);

            String discount = callsDTO.getDiscountForCalls().isEmpty() ? "0" : callsDTO.getDiscountForCalls();
            BigDecimal grossCost = call.calculateGrossCost(durationSeconds).setScale(2, RoundingMode.HALF_UP);
            BigDecimal netCost = call.calculateNetCost(grossCost, discount).setScale(2, RoundingMode.HALF_UP);

            String callDate = LocalDateTime.now().toString();
            String costPerSecond = call.getCostPerSecond();

            Call enterCall = new Call(
                    startTime.toString(), endTime.toString(), String.valueOf(durationSeconds),
                    costPerSecond, discount, call.getVat(),
                    netCost.toString(), grossCost.toString(),
                    callDate, "Pending Invoice",
                    user, callReceiver
            );

            return callRepository.save(enterCall);

        } catch (Exception e) {
            System.err.println("Error creating call: " + e.getMessage());
            e.printStackTrace();
            throw new CallCreationException("Failed to create call due to: " + e.getMessage());
        }
    }

    public List<Call> getCallsByUsernameAndStatus(String username, String status) {
        return callRepository.findByUsernameAndStatus(username, status);
    }

    public List<Call> getCallsByUsername(String username) {
        return callRepository.findByUser_Username(username);
    }
/*
    public BigDecimal calculateTotalAmount(Set<Call> calls) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        System.out.println("netCost in calTotalAOUNT " + netCost);
        for (Call call : calls) {
            if (netCost != null) {
                totalAmount = totalAmount.add(netCost);
            } else {

                logger.warn("Null net cost encountered for call: {}", call);
            }
        }
        logger.info("Total amount: {}", totalAmount);
        return totalAmount;
    }*/

}
