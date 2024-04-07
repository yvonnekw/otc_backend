package com.otc.backend.event;

import com.otc.backend.dto.InvoiceDTO;

public class InvoiceGenerationEvent {

    private String username;
    private InvoiceDTO invoiceWithCallIdsDTO;

    public InvoiceGenerationEvent() {
    }

    public InvoiceGenerationEvent(String username, InvoiceDTO invoiceWithCallIdsDTO) {
        this.username = username;
        this.invoiceWithCallIdsDTO = invoiceWithCallIdsDTO;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InvoiceDTO getInvoiceWithCallIdsDTO() {
        return invoiceWithCallIdsDTO;
    }

    public void setInvoiceWithCallIdsDTO(InvoiceDTO invoiceWithCallIdsDTO) {
        this.invoiceWithCallIdsDTO = invoiceWithCallIdsDTO;
    }
    
}
