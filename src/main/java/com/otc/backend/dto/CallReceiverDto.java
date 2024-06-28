package com.otc.backend.dto;

public class CallReceiverDto {

    private Long callReceiverId;
    private String telephone;
    private String username;
    private String fullName;
    private String relationship;

    public CallReceiverDto() {
    }
/* 
    public CallReceiverDto(String telephone, String username) {
        this.telephone = telephone;
        this.username = username;
    }*/

public CallReceiverDto(String telephone, String username, String fullName, String relationship) {
    this.telephone = telephone;
    this.username = username;
    this.fullName = fullName;
    this.relationship = relationship;
}
    
public CallReceiverDto(Long callReceiverId, String telephone, String username, String fullName, String relationship) {
    this.callReceiverId = callReceiverId;
    this.telephone = telephone;
    this.username = username;
    this.fullName = fullName;
    this.relationship = relationship;
}

    public CallReceiverDto(String fullName2, String telephone2) {
        this.fullName = fullName2;
        this.telephone = telephone2;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "CallReceiverDto{" +
                "callReceiverId=" + callReceiverId +
                ", telephone='" + telephone + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", relationship='" + relationship + '\'' +
                '}';
    }
}