package com.otc.backend.services;


import java.util.List;

import com.otc.backend.dto.InvoiceDto;
import com.otc.backend.models.Invoice;
import org.springframework.http.ResponseEntity;

public interface InvoiceService {

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long invoiceId);

   // Invoice createInvoice(Long invoiceId);

    Invoice updateInvoice(Long invoiceId, Invoice invoiceDetails);

    void deleteInvoice(Long invoiceId);

    Invoice createInvoice(Invoice invoice);

    List<InvoiceDto> getAllInvoicesWithCallIds();

    //Invoice createInvoiceForCalls(Set<Call> calls);
    //Invoice createInvoiceForCalls(Invoice invoice);
    InvoiceDto createInvoiceForCalls(InvoiceDto invoiceDTO);

    List<InvoiceDto> getInvoicesByUsername(String username);

    //ResponseEntity<List<InvoiceDto>> getInvoicesByUsername(String username);
    //List<InvoiceDto> getInvoicesByUsername(String username);

    // BigDecimal createInvoiceForCalls(Set<Call> calls);

   // List<Call> getAllPaidCalls();

   // List<Call> getAllUnpaidCalls();

   //void generateInvoiceForCalls(List<Call> calls);

   // void triggerInvoiceCreation(String username, InvoiceWithCallIdsDTO invoiceWithCallIdsDTO);
    //List<Invoice> getAllInvoices();

    //List<Invoice> findAllInvoicesWithCallsAndUser();

    //List<Invoice> findAllInvoiceCallsUser();

    ///List<Invoice> findAllInvoicesWithCallsAndUsers();
    
}
