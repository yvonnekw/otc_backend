package com.otc.backend.dto;

import com.otc.backend.models.Users;

public class CallReceiverDto {

    private Long callReceiverId;
	private String telephone;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    private Users user;
	
    public CallReceiverDto() {
    }

    public CallReceiverDto(String telephone, Users user) {
        this.telephone = telephone;
        this.user = user;
    }

    public CallReceiverDto(Long callReceiverId, String telephone, Users user) {
        this.callReceiverId = callReceiverId;
        this.telephone = telephone;
        this.user = user;
    }

    public Long getCallReceiverId() {
        return callReceiverId;
    }
    public void setCallReceiverId(Long callReceiverId) {
        this.callReceiverId = callReceiverId;
    }
  
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*
    public CallReceiverDto(Long callReceiverId, String telephone) {
        this.callReceiverId = callReceiverId;
        this.telephone = telephone;
    }*/

    @Override
    public String toString() {
        return "CallReceiverDto [callReceiverId=" + callReceiverId + ", telephone=" + telephone + "]";
    }
   
}
