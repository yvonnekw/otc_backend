package com.otc.backend.dto;

import java.util.List;

public class InvoiceDTO {
    

    private Long invoiceId;
    private String invoiceDate;
    private String status; // Status of the invoice (e.g., "Invoiced", "Paid", "Overdue")
    private String totalAmount; // Total amount of the invoice
    private List<Long> callIds;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceDTO(String invoiceDate, String status, String totalAmount,
                      List<Long> callIds) {
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
