package com.otc.backend.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

public class InvoiceDTO {
    

    private Long invoiceId;
    private String invoiceDate;
    private String status; // Status of the invoice (e.g., "Invoiced", "Paid", "Overdue")
    private String totalAmount; // Total amount of the invoice
    private List<Long> callIds;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String status, List<Long> callIds) {
        this.status = status;
        this.callIds = callIds;
    }

    public InvoiceDTO(Long invoiceId, String invoiceDate, String status, String totalAmount,
            List<Long> callIds) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.callIds = callIds;
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
        return "InvoiceDTO [invoiceId=" + invoiceId + ", invoiceDate=" + invoiceDate + ", status=" + status
                + ", totalAmount=" + totalAmount + ", callIds=" + callIds + "]";
    }

    
    
}
