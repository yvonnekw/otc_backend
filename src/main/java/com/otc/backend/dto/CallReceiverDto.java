package com.otc.backend.dto;

public class CallReceiverDto {

    private Long callReceiverId;
    private String telephone;
    private String username;

    public CallReceiverDto() {
    }

    public CallReceiverDto(String telephone, String username) {
        this.telephone = telephone;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CallReceiverDto{" +
                "callReceiverId=" + callReceiverId +
                ", telephone='" + telephone + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}