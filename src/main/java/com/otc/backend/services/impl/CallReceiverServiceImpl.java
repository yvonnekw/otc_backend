package com.otc.backend.services.impl;

import java.util.List;

import com.otc.backend.services.CallReceiverService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.otc.backend.dto.CallReceiverDto;
import com.otc.backend.exception.CallReceiverCreationException;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.CallReceiver;
import com.otc.backend.models.Users;
import com.otc.backend.repository.CallReceiverRepository;
import com.otc.backend.repository.CallRepository;
import com.otc.backend.repository.UserRepository;

@Service
@Transactional
public class CallReceiverServiceImpl implements CallReceiverService {

    private final CallRepository callRepository;
    private final UserRepository userRepository;
    private final CallReceiverRepository callReceiverRepository;

    public CallReceiverServiceImpl(CallRepository callRepository, UserRepository userRepository,
                                   CallReceiverRepository callReceiverRepository) {
        this.callRepository = callRepository;
        this.userRepository = userRepository;
        this.callReceiverRepository = callReceiverRepository;
    }

    public CallReceiver addCallReceiver(String username, CallReceiverDto callReceiverDTO) {
        try {
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserDoesNotExistException());
            CallReceiver callReceiver = new CallReceiver();
            callReceiver.setFullName(callReceiverDTO.getFullName());
            callReceiver.setTelephone(callReceiverDTO.getTelephone());
            callReceiver.setRelationship(callReceiverDTO.getRelationship());
            callReceiver.setUser(user);
            System.out.println("Call Receiver details: " + callReceiver);

            return callReceiverRepository.save(callReceiver);
        } catch (UserDoesNotExistException e) {
            throw new CallReceiverCreationException("User does not exist", e);
        } catch (Exception e) {
            throw new CallReceiverCreationException("An error occurred while adding the call receiver", e);
        }
    }

    @Override
    public boolean isPhoneNumberRegisteredForUser(String username, String telephone) {
        return callReceiverRepository.existsByUserUsernameAndTelephone(username, telephone);
    }

    @Override
    public boolean checkPhoneNumberExistsForUser(String username, String telephone) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkPhoneNumberExistsForUser'");
    }

    @Override
    public List<CallReceiver> getCallReceiversByUsername(String username) {
        return callReceiverRepository.findByUserUsername(username);
    }

    public List<String> getDistinctPhoneNumbersForUser(String username) {
        return callReceiverRepository.findDistinctTelephoneByUser_Username(username);
    }

    public List<String> findDistinctTelephoneByUserUsername(String username) {
        return callReceiverRepository.findDistinctTelephoneByUserUsername(username);
    }

    public List<CallReceiver> getAllCallReceivers() {
        return callReceiverRepository.findAll();
    }

}