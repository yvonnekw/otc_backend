package com.otc.backend.services;

import com.otc.backend.dto.LoginResponseDto;
import com.otc.backend.models.Users;



public interface AuthenticationService {

   // public Users registerUser(String username, String password, String emailAddress, String telephone);

    public LoginResponseDto loginUser(String username, String password);

}
