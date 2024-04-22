package com.otc.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Transient;

public class CallsInvoice {


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

    @Transient
    public String getTotalPrice() {
        return getCall().getNetCost();
    }

}
