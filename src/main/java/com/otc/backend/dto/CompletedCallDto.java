package com.otc.backend.dto;

public class CompletedCallDto {
    private Long currentCallId;
    private String startTime;
    private String endTime;


    public CompletedCallDto() {
    }

    public CompletedCallDto(Long currentCallId, String startTime, String endTime) {
        this.currentCallId = currentCallId;
        this.startTime = startTime;
        this.endTime = endTime;
     
    }

    public Long getCurrentCallId() {
        return currentCallId;
    }

    public void setCurrentCallId(Long long1) {
        this.currentCallId = long1;
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

    @Override
    public String toString() {
        return "CompletedCallDto [currentCallId=" + currentCallId + ", startTime=" + startTime + ", endTime=" + endTime
                + "]";
    }


}
