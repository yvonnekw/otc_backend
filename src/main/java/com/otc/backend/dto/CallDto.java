package com.otc.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.otc.backend.models.CallReceiver;
import com.otc.backend.models.Users;

public class CallDto {

    private static final BigDecimal RATE_PER_SECOND = new BigDecimal("0.01");

    private String startTime;
    private String endTime;
    private String duration;
    private String costPerSecond;
    private String discountForCalls;
    private String vat;
    private String netCost;
    private String grossCost;
    private String callDate;
    private String status;

    private Long callReceiverId;
    private Set<Long> callUser;

    public CallDto(String startTime, String endTime, String duration, String costPerSecond, String discountForCalls, String vat, String netCost, String grossCost, String callDate, String status, Long callReceiverId, Set<Long> callUser) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.costPerSecond = costPerSecond;
        this.discountForCalls = discountForCalls;
        this.vat = vat;
        this.netCost = netCost;
        this.grossCost = grossCost;
        this.callDate = callDate;
        this.status = status;
        this.callReceiverId = callReceiverId;
        this.callUser = callUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCallReceiverId() {
        return callReceiverId;
    }

    public void setCallReceiverId(Long callReceiverId) {
        this.callReceiverId = callReceiverId;
    }

    public Set<Long> getCallUser() {
        return callUser;
    }

    public void setCallUser(Set<Long> callUser) {
        this.callUser = callUser;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }


    public String getCostPerSecond() {
        return costPerSecond;
    }

    public void setCostPerSecond(String costPerSecond) {
        this.costPerSecond = RATE_PER_SECOND.toString();
    }

    public String getDiscountForCalls() {
        return discountForCalls;
    }

    public void setDiscountForCalls(String discountForCalls) {
        this.discountForCalls = discountForCalls;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getNetCost() {
        return netCost;
    }

    public void setNetCost(String netCost) {
        this.netCost = netCost;
    }

    public String getGrossCost() {
        return grossCost;
    }

    public void setGrossCost(String grossCost) {
        this.grossCost = grossCost;
    }

    public CallDto() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getDuration() {
        return duration;
    }
 
    public void setDuration(String duration) {
        this.duration = duration;
    }



  

}
