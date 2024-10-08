package com.otc.backend.models;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Duration;

@Entity
@Table(name = "call")
public class Call {

    private static final BigDecimal RATE_PER_SECOND = new BigDecimal("0.01");

    private static final BigDecimal TAX_RATE = new BigDecimal("20");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long callId;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private CallReceiver receiver;

    public Call() {
    }

    public Call(String startTime, String endTime, String duration, String costPerSecond, String discountForCalls,
                String vat, String netCost, String grossCost, String callDate, String status, Users user, CallReceiver receiver) {
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
        this.user = user;
        this.receiver = receiver;
    }


    public Call(String startTime, String endTime, String discountForCalls, Users user, CallReceiver receiver) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountForCalls = discountForCalls;
        this.user = user;
        this.receiver = receiver;
    }


    public Call(Long callId, String startTime, String endTime, String duration, String costPerSecond,
                String discountForCalls, String vat, String netCost, String grossCost, String totalCost, String callDate,
                String status, Users user, CallReceiver receiver) {
        this.callId = callId;
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
        this.user = user;
        this.receiver = receiver;
    }

    public String getCostPerSecond() {
        return costPerSecond;
    }


    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
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


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setCostPerSecond(String costPerSecond) {
        this.costPerSecond = costPerSecond;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public CallReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(CallReceiver receiver) {
        this.receiver = receiver;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public long calculateDurationInSeconds(LocalTime startTime, LocalTime endTime) {
        try {

            System.out.println("Start time in calculateDurationInSeconds: " + startTime);
            System.out.println("End time in calculateDurationInSeconds: " + endTime);

            long durationSeconds = Duration.between(startTime, endTime).getSeconds();

            return durationSeconds;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public BigDecimal calculateGrossCost(long durationSeconds) {
        try {
            BigDecimal costPerSecond = (RATE_PER_SECOND);

            setCostPerSecond(costPerSecond.toString());

            BigDecimal costPerSecondDecimal = (costPerSecond);

            BigDecimal grossCost = costPerSecondDecimal
                    .multiply(BigDecimal.valueOf(durationSeconds));

            System.out.println("grossCost " + grossCost);
            return grossCost;

        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }


    public BigDecimal calculateNetCost(BigDecimal grossCost, String discount) {
        try {

            BigDecimal vat = TAX_RATE.multiply(grossCost).divide(BigDecimal.valueOf(100));
            System.out.println("vat  " + vat);
            setVat(vat.toString());
            BigDecimal netCost = new BigDecimal("0.00");
            System.out.println("discount for calls  " + discount);
            System.out.println("vat cal  " + vat);

            BigDecimal discounts = new BigDecimal(discount);

            if (discounts.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal calculateDiscount = grossCost.multiply(discounts).divide(BigDecimal.valueOf(100));
                System.out.println("netCost With Discount " + calculateDiscount);

                netCost = grossCost.subtract(calculateDiscount).add(vat);

            } else {
                netCost = grossCost.add(vat);
            }

            System.out.println("net cost " + netCost);

            return netCost;

        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }


    }

    @Override
    public String toString() {
        return "Call{" +
                "callId=" + callId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration='" + duration + '\'' +
                ", costPerSecond='" + costPerSecond + '\'' +
                ", discountForCalls='" + discountForCalls + '\'' +
                ", vat='" + vat + '\'' +
                ", netCost='" + netCost + '\'' +
                ", grossCost='" + grossCost + '\'' +
                ", callDate='" + callDate + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", receiver=" + receiver +
                '}';
    }
}
