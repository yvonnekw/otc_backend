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
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Call;
import com.otc.backend.models.CallReceiver;
import com.otc.backend.models.Users;
import com.otc.backend.repository.CallReceiverRepository;
import com.otc.backend.repository.CallRepository;
import com.otc.backend.repository.UserRepository;


public interface CallReceiverService {

    public CallReceiver addCallReceiver(String username, CallReceiverDto callReceiverDTO);

    public List<String> findDistinctTelephoneByUserUsername(String username);

    public boolean isPhoneNumberRegisteredForUser(String username, String telephone);


    public boolean checkPhoneNumberExistsForUser(String username, String telephone);


    public List<CallReceiver> getCallReceiversByUsername(String username);

    
}
