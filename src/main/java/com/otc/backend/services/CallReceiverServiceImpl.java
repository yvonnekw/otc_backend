package com.otc.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.otc.backend.controller.CallReceiverController;
import com.otc.backend.dto.CallDto;
import com.otc.backend.dto.CallReceiverDto;
import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.exception.CallReceiverCreationException;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Call;
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

    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * public boolean checkPhoneNumberExistsForUser(String username, String
     * telephone) {
     * return callReceiverRepository.existsByUserUsernameAndTelephone(username,
     * telephone);
     * }
     * 
     * 
     * 
     * public List<CallReceiver> getCallReceiversByUsername(String username) {
     * return callReceiverRepository.findByUserUsername(username);
     * }
     */

    // getDistinctPhoneNumbersByUsername

    /*
     * public List<String> getDistinctPhoneNumbersForUser(String username) {
     * return callReceiverRepository.findDistinctTelephoneByUserUsername(username);
     * }
     */
    /*
     * public List<String> getPhoneNumbersForUser(String username) {
     * List<CallReceiver> callReceivers =
     * callReceiverRepository.findByUserUsername(username);
     * List<String> phoneNumbers = new ArrayList<>();
     * for (CallReceiver receiver : callReceivers) {
     * phoneNumbers.add(receiver.getTelephone());
     * }
     * return phoneNumbers;
     * }
     */

    // public List<CallReceiver> getCallReceiversForUser(String username) {
    // return callRepository.findByCallUser(username);
    // }

    /*
     * 
     * public Call makeCall(CallDto callDTO) {
     * Call call = new Call();
     * Call saveCall = callRepository.save(call);
     * return (saveCall);
     * }
     * 
     * public Call getCallById(Long callId) {
     * return
     * callRepository.findById(callId).orElseThrow(UserDoesNotExistException::new);
     * }
     * 
     * public List<Call> getAllCalls() {
     * return callRepository.findAll();
     * }
     * 
     * public Call updateCall(Long callId, Call updatedCall){
     * 
     * Call call = callRepository.findById(callId).orElseThrow(
     * () -> new ResourceNotFoundException("Call not found with the given Id : " +
     * callId)
     * );
     * call.setStartTime(updatedCall.getStartTime());
     * call.setEndTime(updatedCall.getEndTime());
     * 
     * Call updatedCallObj = callRepository.save(call);
     * return updatedCallObj;
     * }
     * 
     * public void deleteCall(Long callId) {
     * CallUser user = userRepository.findById(callId).orElseThrow(
     * () -> new ResourceNotFoundException("User not found with the given Id : " +
     * callId)
     * );
     * 
     * callRepository.deleteById(callId);
     * }
     * 
     * public CallService(CallRepository callsRepository, UserRepository
     * userRepository){
     * this.callRepository = callsRepository;
     * this.userRepository = userRepository;
     * }
     * 
     * public Call makeCall(String username, CallDto callsDTO) throws Exception {
     * CallUser user =
     * userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException
     * ::new);
     * System.out.println("user name " + username);
     * 
     * Call call = new Call();
     * try {
     * call.setStartTime(callsDTO.getStartTime());
     * call.setEndTime(callsDTO.getEndTime());
     * call.setDuration(callsDTO.getDuration());
     * call.setCallUser(user);
     * 
     * System.out.println("call details " + call);
     * System.out.println("user details  " + user);
     * 
     * return callRepository.save(call);
     * } catch (Exception e) {
     * e.getStackTrace();
     * }
     * 
     * return null;
     * }
     */

}
