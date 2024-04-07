package com.otc.backend.dto;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Document;

import com.otc.backend.models.Role;

@Document(indexName ="user")
public class RegistrationDto {

    private long userId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String username;
    private String password;
	private String telephone;
    private Set<Role> authorities;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public RegistrationDto(){
        super();
    }

    public String getEmailAdress() {
        return emailAddress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAddress = emailAdress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
/* 
    public RegistrationDTO(String username, String password){
        super();
        this.username = username;
        this.password = password;
    }*/

    public RegistrationDto(String username, String password, Set<Role> authoritie) {
		this.username = username;
		this.password = password;
	}

    public RegistrationDto(String firstName, String lastName, String emailAddress, String password,
            String telephone, Set<Role> authorities) {
        super();

        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.telephone = telephone;
        this.authorities = authorities;
    }

    public RegistrationDto(String username, String firstName, String lastName, String emailAddress, String password,
                           String telephone,  Set<Role> authorities) {
        super();
         this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.telephone = telephone;
        this.authorities = authorities;
    }
/* 
    public RegistrationDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }*/

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

   
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "RegistrationDto [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", emailAddress=" + emailAddress + ", username=" + username + ", password=" + password
                + ", telephone=" + telephone + ", authorities=" + authorities + "]";
    }

    
}
