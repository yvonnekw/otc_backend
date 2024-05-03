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
    
    // Rate charged per second for the call
    private static final BigDecimal RATE_PER_SECOND = new BigDecimal("0.01");

    // Tax rate applied to the call
    private static final BigDecimal TAX_RATE = new BigDecimal("0.2");

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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


    //Calculations of a call
    public long calculateDurationInSeconds(LocalTime startTime, LocalTime endTime) {
        try {

            System.out.println("Start time in calculateDurationInSeconds: " + startTime);
            System.out.println("End time in calculateDurationInSeconds: " + endTime);
            // Convert startTime and endTime to LocalTime
          // LocalTime start = LocalTime.parse(startTime);
            //System.out.println("Start time in calculateDurationInSeconds: " + start);
          // LocalTime end = LocalTime.parse(endTime);
           // System.out.println("End time in calculateDurationInSeconds: " + end);

            // Calculate the duration of the call in seconds
           long durationSeconds = Duration.between(startTime, endTime).getSeconds();

            return durationSeconds;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Handle error appropriately in your code
        }
    }


    public BigDecimal applyDiscount(BigDecimal netCost, String discount) {
        try {
            // Convert discountForCalls to BigDecimal
            BigDecimal discounts = new BigDecimal(discount);

            // Apply discount if available
            if (discounts.compareTo(BigDecimal.ZERO) > 0) {
                netCost = netCost.subtract(netCost.multiply(discounts));
            }

            return netCost;
        } catch (Exception e) {
            e.printStackTrace();
            return netCost; // Handle error appropriately in your code
        }
    }

    public BigDecimal calculateVATAmount(BigDecimal netCost) {
        try {
            // Convert vat to BigDecimal
            BigDecimal vat = TAX_RATE;

            // Calculate VAT amount
            BigDecimal vatAmount = netCost.multiply(vat);

            return vatAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO; // Handle error appropriately in your code
        }
    }

    public BigDecimal calculateGrossCost(BigDecimal netCost, BigDecimal vatAmount) {
        try {
            // Calculate gross cost
            BigDecimal grossCost = netCost.add(vatAmount);
            return grossCost;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO; // Handle error appropriately in your code
        }
    }


    public BigDecimal calculateNetCost(long durationSeconds) {
        try {

            //calculateDurationInSeconds();
            // Calculate the duration of the call in seconds
          //  long durationSeconds = calculateDurationInSeconds(startTime, endTime);

          // Convert costPerSecond to BigDecimal
            
          BigDecimal costPerSecond = (RATE_PER_SECOND);
          // setCostPerSecond = parse.valueOf(costPerSecond);
          // this.costPerSecond = new BigDecimal(costPerSecond).toString();
          // setCostPerSecond(costPerSecond).toString();
         
          setCostPerSecond(costPerSecond.toString());

          //System.out.print.valueOf(setCostPerSecond(costPerSecond.toString()));
            
          BigDecimal costPerSecondDecimal = (costPerSecond);

            // Calculate the net cost of the call
            BigDecimal netCost = costPerSecondDecimal
                    .multiply(BigDecimal.valueOf(durationSeconds))
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

            return netCost;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO; // Handle error appropriately in your code
        }

    
    }

    @Override
    public String toString() {
        return "Call [callId=" + callId + ", startTime=" + startTime + ", endTime=" + endTime + ", duration=" + duration
                + ", costPerMinute=" + costPerSecond + ", discountForCalls=" + discountForCalls + ", vat=" + vat
                + ", netCost=" + netCost + ", grossCost=" + grossCost + ", callDate=" + callDate + ", status=" + status
                + ", user=" + user + ", receiver=" + receiver + "]";
    }
  
    

}
