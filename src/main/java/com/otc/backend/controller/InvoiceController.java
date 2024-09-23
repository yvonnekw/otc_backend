package com.otc.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.otc.backend.dto.CallDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otc.backend.dto.InvoiceDto;
import com.otc.backend.models.Invoice;
import com.otc.backend.models.Call;
import com.otc.backend.repository.InvoiceRepository;
import com.otc.backend.services.InvoiceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/invoices")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping("/create-in")
    public ResponseEntity<String> createInvoices(@RequestBody Invoice invoice) {

        System.out.println("Received calls: body " + invoice);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    ;


    @PostMapping("/create-invoice")
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDTO) {
        try {

            logger.info("Invoice data coming in from create invoice controller: " + invoiceDTO);
            InvoiceDto createdInvoice = invoiceService.createInvoiceForCalls(invoiceDTO);
            logger.info("Invoice created from create invoice controller: " + createdInvoice);

            return ResponseEntity.ok(createdInvoice);
        } catch (Exception e) {
            System.err.println("Error creating invoice from create invoice controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<InvoiceDto>> getInvoicesByUsername(@PathVariable String username) {
        List<InvoiceDto> invoices = invoiceService.getInvoicesByUsername(username);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/get-all-invoice")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/get-invoice-callIds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllInvoicesWithCallIds() {
        try {
            List<InvoiceDto> invoicesWithCallIds = invoiceService.getAllInvoicesWithCallIds();
            return ResponseEntity.ok(invoicesWithCallIds);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return an error response entity
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching invoices with call IDs.");
        }
    }

    @GetMapping("/{invoiceId}")
    @PreAuthorize("hasRole('ADMIN', USER)")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long invoiceId) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }
}
