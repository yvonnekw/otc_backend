package com.otc.backend.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    private String invoiceDate;
    //private String amount;
    //  private boolean isPaid;
    private String status; // Status of the invoice (e.g., "Invoiced", "Paid", "Overdue")
  
    private String totalAmount; // Total amount of the invoice

    @ManyToMany
    @JoinTable(name = "invoice_calls", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "call_id"))
    private Set<Call> calls = new HashSet<>();

    /* 
    @ElementCollection
    @CollectionTable(name = "invoice_calls", joinColumns = @JoinColumn(name = "invoice_id"))
    @Column(name = "call_id")
    private Set<Call> calls = new HashSet<>(); // IDs of the related calls
    */
   
   // @ManyToMany
   // @JoinTable(name = "invoice_call", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "call_id"))
   // private Set<Call> calls = new HashSet<>();

    // Associate the invoice directly with the current call
    //@OneToOne
   // @JoinColumn(name = "current_call_id")
   // private CurrentCall currentCall;

   public Invoice() {
   }
    
   public Invoice( String status, String totalAmount, Set<Call> calls) {
       this.status = status;
       this.totalAmount = totalAmount;
       this.calls = calls;
   }


    public Invoice(Long invoiceId, String invoiceDate, String status, String totalAmount, Set<Call> calls) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.calls = calls;
    }


    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }


    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


    public Set<Call> getCalls() {
        return calls;
    }

    public void setCalls(Set<Call> calls) {
        this.calls = calls;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Invoice [invoiceId=" + invoiceId + ", invoiceDate=" + invoiceDate + ", status=" + status
                + ", totalAmount=" + totalAmount + ", callIds=" + calls + "]";
    }


   
    
}
