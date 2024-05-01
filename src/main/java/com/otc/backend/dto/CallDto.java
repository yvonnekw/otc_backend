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
    // private String totalCost;
    private String callDate;
    private String status;

    private CallReceiver callReceiver;
    private Set<Users> callUsers;

    public CallDto(String startTime, String endTime, String discountForCalls, CallReceiver callReceiver,
            Set<Users> callUsers) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountForCalls = discountForCalls;
        this.callReceiver = callReceiver;
        this.callUsers = callUsers;
    }

    public CallDto(String startTime, String endTime, String duration, String costPerSecond, String discountForCalls,
            String vat, String netCost, String grossCost, String totalCost, String callDate,
            CallReceiver callReceiver, Set<Users> callUsers) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.costPerSecond = costPerSecond;
        this.discountForCalls = discountForCalls;
        this.vat = vat;
        this.netCost = netCost;
        this.grossCost = grossCost;
      //  this.totalCost = totalCost;
        this.callDate = callDate;
        this.callReceiver = callReceiver;
        this.callUsers = callUsers;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public Set<Users> getCallUsers() {
        return callUsers;
    }

    public void setCallUsers(Set<Users> callUsers) {
        this.callUsers = callUsers;
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

    public CallReceiver getCallReceiver() {
        return callReceiver;
    }

    public void setCallReceiver(CallReceiver callReceiver) {
        this.callReceiver = callReceiver;
    }

    @Override
    public String toString() {
        return "CallDto [startTime=" + startTime + ", endTime=" + endTime + ", duration=" + duration
                + ", costPerSecond=" + costPerSecond + ", discountForCalls=" + discountForCalls + ", vat=" + vat
                + ", netCost=" + netCost + ", grossCost=" + grossCost + ", callDate=" + callDate + ", status=" + status
                + ", callReceiver=" + callReceiver + ", callUsers=" + callUsers + "]";
    }

  

}
