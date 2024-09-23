package com.otc.backend.dto;

import com.otc.backend.models.Users;

public class LoginResponseDto {
    private Users user;
    private String token;

    public LoginResponseDto() {
        super();
    }

    public LoginResponseDto(Users user, String token) {
        this.user = user;
        this.token = token;
    }

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponseDto [user=" + user + ", token=" + token + "]";
    }


}
