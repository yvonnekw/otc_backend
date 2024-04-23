package com.otc.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "calls_to_invoice")
public  class CallsInvoice {


    @EmbeddedId
    @JsonIgnore
    private CallsToInvoicePK callsToInvoicePK;

    CallsInvoice(Invoice invoice, Call call) {
        callsToInvoicePK = new CallsToInvoicePK();
        callsToInvoicePK.setInvoice(invoice);
        callsToInvoicePK.setCall(call);
    }
    
    @Transient
    public Call getCall() {
        return this.callsToInvoicePK.getCall();
    }

   // @Transient
 //   public String getTotalPrice() {
       // return getCall().getNetCost();
   // }

    public CallsToInvoicePK getCallsToInvoicePK() {
        return callsToInvoicePK;
    }

    public void setCallsToInvoicePK(CallsToInvoicePK callsToInvoicePK) {
        this.callsToInvoicePK = callsToInvoicePK;
    }



}


