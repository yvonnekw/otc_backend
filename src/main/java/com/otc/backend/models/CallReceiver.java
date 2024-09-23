package com.otc.backend.models;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "call_receiver")
public class CallReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long callReceiverId;
    private String telephone;
    private String fullName;
    private String relationship;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public CallReceiver(Long callReceiverId, String telephone, String fullName, String relationship, Users user) {
        this.callReceiverId = callReceiverId;
        this.telephone = telephone;
        this.fullName = fullName;
        this.relationship = relationship;
        this.user = user;
    }

    public CallReceiver() {
    }

    public CallReceiver(String telephone, String fullName, String relationship, Users user) {
        this.telephone = telephone;
        this.fullName = fullName;
        this.relationship = relationship;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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
        return "CallReceiver{" +
                "callReceiverId=" + callReceiverId +
                ", telephone='" + telephone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", user=" + user +
                '}';
    }
}
