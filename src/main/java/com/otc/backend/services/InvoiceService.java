package com.otc.backend.services;


import java.util.List;

import com.otc.backend.dto.InvoiceDto;
import com.otc.backend.models.Invoice;
import org.springframework.http.ResponseEntity;

public interface InvoiceService {

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long invoiceId);

    Invoice updateInvoice(Long invoiceId, Invoice invoiceDetails);

    void deleteInvoice(Long invoiceId);

    Invoice createInvoice(Invoice invoice);

    List<InvoiceDto> getAllInvoicesWithCallIds();

    InvoiceDto createInvoiceForCalls(InvoiceDto invoiceDTO);

    List<InvoiceDto> getInvoicesByUsername(String username);

}
