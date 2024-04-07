package com.otc.backend.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otc.backend.dto.InvoiceDTO;
import com.otc.backend.models.Call;
import com.otc.backend.models.Invoice;
import com.otc.backend.repository.InvoiceRepository;
import com.otc.backend.services.InvoiceService;
import com.otc.backend.services.InvoiceServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/invoices")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    //@Autowired
    private final InvoiceService invoiceService;
   // @Autowired
    private final InvoiceRepository invoiceRepository;

    //@Autowired
    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping("/create-in")
    public ResponseEntity<String> createInvoices(@RequestBody Invoice invoice) {
        //try {
            // Log the received set of calls
            System.out.println("Received calls: body "+ invoice);

            return new ResponseEntity<>( HttpStatus.CREATED);

        };


        @PostMapping("/create-invoice")
        public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
            // Invoice createdInvoice = invoiceService.createInvoice(invoice);
            // return ResponseEntity.ok(createdInvoice);
            try {

                logger.info("Invoice data comming in : " + invoice);
                // Call the service method to create the invoice
                 Invoice createdInvoice = invoiceService.createInvoice(invoice);
               // InvoiceDTO createdInvoice = invoiceService.createInvoiceForCalls(invoiceDTO);
                logger.info("Invoice created: " + createdInvoice);

                // Return the created invoice with HTTP status code 200 (OK)
                return ResponseEntity.ok(createdInvoice);
                //return ResponseEntity.ok(createdInvoice);
            } catch (Exception e) {
                // Log any exceptions that occur during invoice creation
                System.err.println("Error creating invoice: " + e.getMessage());
                e.printStackTrace(); // Print the stack trace for detailed error information
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        /* 
        @PostMapping("/create-invoice")
        public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
            //Invoice createdInvoice = invoiceService.createInvoice(invoice);
            //return ResponseEntity.ok(createdInvoice);
            try {
                // Call the service method to create the invoice
                //Invoice createdInvoice = invoiceService.createInvoice(invoice);
                Invoice createdInvoice = invoiceService.createInvoiceForCalls(invoice);
                System.out.println("Invoice created: " + createdInvoice);

                // Return the created invoice with HTTP status code 200 (OK)
                return ResponseEntity.ok(createdInvoice);
            } catch (Exception e) {
                // Log any exceptions that occur during invoice creation
                System.err.println("Error creating invoice: " + e.getMessage());
                e.printStackTrace(); // Print the stack trace for detailed error information
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
     */

/* 
    @PostMapping("/create-invoice")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Set<Call> calls) {
        try {
            // Log the received set of calls
            System.out.println("Received calls: " + calls);

            // Call the service method to create the invoice
            System.out.println("Creating invoice...");
            Invoice invoice = invoiceService.createInvoiceForCalls(calls);
           // System.out.println("Invoice created: " + invoice);

            // Return the created invoice with HTTP status code 201 (Created)
            return new ResponseEntity<>(invoice, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log any exceptions that occur during invoice creation
            System.err.println("Error creating invoice: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/
  @GetMapping("/all")
  public List<Invoice> getAllInvoices() {
      return invoiceRepository.findAll();
  }

    @GetMapping("/get-all-invoice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllInvoicesWithCallIds() {
        try {
            List<InvoiceDTO> invoicesWithCallIds = invoiceService.getAllInvoicesWithCallIds();
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

    /* 
    @PostMapping("/create-invoice")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.ok(createdInvoice);
    }

    */
/* 
    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long invoiceId, @RequestBody Invoice invoiceDetails) {
        Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, invoiceDetails);
        return ResponseEntity.ok(updatedInvoice);
    }*/

  /*
 * @GetMapping("/get-all-invoice")
 * public ResponseEntity<List<Invoice>> getAllInvoices() {
 * List<Invoice> invoices = invoiceService.getAllInvoices();
 * return ResponseEntity.ok(invoices);
 * }
 */
/*
 * @GetMapping("/get-all-invoice")
 * public ResponseEntity<?> getAllInvoices() {
 * try {
 * List<Invoice> invoices = invoiceService.getAllInvoices();
 * return ResponseEntity.ok(invoices);
 * } catch (Exception e) {
 * // Log the exception for debugging purposes
 * e.printStackTrace();
 * // Return an error response entity
 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 * .body("An error occurred while fetching invoices.");
 * }
 * }
 */
    
}
