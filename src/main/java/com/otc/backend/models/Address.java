package com.otc.backend.models;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer addressId;
  
     @Column(nullable = false, columnDefinition = "varchar(150)", unique = false)
     private String address1;

     @Column(nullable = true, columnDefinition = "varchar(150)", unique = false)
     private String address2;

     @Column(nullable = true, columnDefinition = "varchar(150)", unique = false)
     private String address3;

    @Column(nullable = false, columnDefinition = "varchar(150)", unique = false)
    private String postcode;

    //mention
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users callerUser;

    public Users getCallerUser() {
        return callerUser;
    }

    public void setCallerUser(Users callerUser) {
        this.callerUser = callerUser;
    }
/* 
    public CallerUser getUser() {
        return applicationUser;
    }

    public void setUser(CallerUser applicationUser) {
        this.applicationUser = applicationUser;
    }
*/
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", address1=" + address1 + ", address2=" + address2 + ", address3="
                + address3 + ", postcode=" + postcode + ", callerUser=" + callerUser + "]";
    }


}
