package com.otc.backend.dto;

import java.util.List;

public class InvoiceDto {
    

    private Long invoiceId;
    private String invoiceDate;
    private String status;
    private String totalAmount;
    private List<Long> callIds;
    private String username;

    public InvoiceDto() {
    }

    public InvoiceDto(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceDto(String invoiceDate, String status, String totalAmount,
                      List<Long> callIds) {
        this.invoiceDate = invoiceDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.callIds = callIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    public String getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
  
    public List<Long> getCallIds() {
        return callIds;
    }

    public void setCallIds(List<Long> callIds) {
        this.callIds = callIds;
    }

    @Override
    public String toString() {
        return "InvoiceDto{" +
                "invoiceId=" + invoiceId +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", callIds=" + callIds +
                ", username='" + username + '\'' +
                '}';
    }
}
