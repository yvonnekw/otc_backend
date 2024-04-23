package com.otc.backend.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import com.otc.backend.dto.InvoiceDTO;
import com.otc.backend.models.Call;
import com.otc.backend.models.Invoice;
import com.otc.backend.repository.CallRepository;
import com.otc.backend.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;
    private final CallRepository callRepository;
    private final CallService callService;
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, CallRepository callRepository, 
            CallService callService) {
        this.invoiceRepository = invoiceRepository;
        this.callRepository = callRepository;
        this.callService = callService;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        try {
            return invoiceRepository.findAll();
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // You can throw a custom exception or return an empty list based on your
            // application logic
            throw new RuntimeException("An error occurred while fetching invoices.", e);
        }
    }

    @Override
    public Invoice getInvoiceById(Long invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        return invoiceOptional.orElse(null); // Return null if invoice is not found
    }

    /* 
    @Override
    public Invoice createInvoice(Invoice invoice) {
        // You may want to add additional logic here before saving the invoice
        return invoiceRepository.save(invoice);
    }
    */

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceDate(LocalDate.now().toString());
 
        Invoice savedInvoice = invoiceRepository.save(invoice);

      //  Set<Call> calls = savedInvoice.getCalls();

        //for (Call call : calls) {
            //call.setInvoiced(true);
            //callRepository.save(call);
        //}

        return savedInvoice;
    }

    
    @Override
    public Invoice updateInvoice(Long invoiceId, Invoice invoiceDetails) {
        // Check if the invoice exists
        Invoice existingInvoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        // Update the existing invoice with the details provided
        existingInvoice.setTotalAmount(invoiceDetails.getTotalAmount());
        // Update other fields as needed

        // Save and return the updated invoice
        return invoiceRepository.save(existingInvoice);
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        // Check if the invoice exists
        Invoice existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        // Delete the invoice
        invoiceRepository.delete(existingInvoice);
    }
    
    @Override
    public List<InvoiceDTO> getAllInvoicesWithCallIds() {
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            List<InvoiceDTO> dtos = new ArrayList<>();

            for (Invoice invoice : invoices) {
                //  List<Long> callIds = invoice.getCalls().stream().map(call -> call.getCallId())
                // .collect(Collectors.toList());
                InvoiceDTO dto = new InvoiceDTO();
                dto.setInvoiceId(invoice.getInvoiceId());
                dto.setInvoiceDate(invoice.getInvoiceDate());
                //  dto.setAmount(invoice.getAmount());
                //  dto.setCallIds(callIds);
                dtos.add(dto);
            }

            return dtos;
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // You can throw a custom exception or return an empty list based on your
            // application logic
            throw new RuntimeException("An error occurred while fetching invoices with call IDs.", e);
        }
    }

    public InvoiceDTO createInvoiceForCalls(InvoiceDTO invoiceDTO) {
        try {

            // Check if callIds list is null
            if (invoiceDTO.getCallIds() == null) {
                throw new IllegalArgumentException("CallIds list must not be null.");
            }
            // Extract call IDs from the DTO
            List<Long> callIds = invoiceDTO.getCallIds();
            logger.info("Call ids {}", callIds);

            // Retrieve calls from the repository using the call IDs
            Set<Call> calls = new HashSet<>();
            for (Long callId : callIds) {
                Optional<Call> optionalCall = callRepository.findById(callId);
                optionalCall.ifPresent(calls::add);
            }

            // Calculate total amount
            BigDecimal totalAmount = callService.calculateTotalAmount(calls);
            logger.info("Total amount calculated: {}", totalAmount);

            // Update status of associated calls
            for (Call call : calls) {
                call.setStatus("Invoiced");
                callRepository.save(call);
                logger.info("Call status updated: {}", call);
            }

            // Save invoice entity
            Invoice invoice = new Invoice();
            invoice.setInvoiceDate(LocalDateTime.now().toString());
            invoice.setStatus("Invoiced");
            invoice.setTotalAmount(totalAmount.toString());
          //  invoice.setCalls(new HashSet<>(calls));
            invoice = invoiceRepository.save(invoice);

            // Update the InvoiceDTO with the saved invoice details
            invoiceDTO.setInvoiceId(invoice.getInvoiceId());
            invoiceDTO.setInvoiceDate(invoice.getInvoiceDate());
            invoiceDTO.setStatus(invoice.getStatus());
            invoiceDTO.setTotalAmount(invoice.getTotalAmount());
            // You may need to set other fields if they were updated by the repository

            return invoiceDTO;
        } catch (Exception e) {
            // Handle exceptions
            // Log and throw or return an appropriate error response
            logger.error("Error creating invoice: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create invoice", e);
        }
    }

/* 
    public Invoice createInvoiceForCalls(Invoice invoice) {
        try {
            // Extract the Call objects from the provided invoice
            Set<Call> calls = invoice.getCalls();
            logger.info("Calls from invoice in createInvoiceForCalls: {}", calls);

            // Check if calls exist
            if (calls == null || calls.isEmpty()) {
                throw new IllegalArgumentException("Calls must not be null or empty.");
            }

            // Calculate the total amount for the invoice using the CallService
            BigDecimal totalAmount = callService.calculateTotalAmount(calls);
            logger.info("Total amount calculated: {}", totalAmount);

            // Update the invoice details
            invoice.setInvoiceDate(LocalDateTime.now().toString());
            invoice.setTotalAmount(totalAmount.toString());
            invoice.setStatus("Invoiced"); // Set the initial status of the invoice
            logger.info("Updated invoice details: {}", invoice);

            // Save the invoice to the database
            invoice = invoiceRepository.save(invoice);
            logger.info("Invoice saved to database: {}", invoice);

            /* 
            // Update the status of each call to indicate that it has been invoiced
            for (Call call : calls) {
            if (call.getCallId().equals(invoice.getCalls.callIds())) {
                call.setStatus("Invoiced");
                callRepository.save(call);
                logger.info("Call status updated: {}", call);
            
            }
            
            
            }

            // Update the status of each call to indicate that it has been invoiced
            for (Call call : invoice.getCalls()) {
                call.setStatus("Invoiced");
                callRepository.save(call);
                logger.info("Call status updated: {}", call);
            }

            return invoice;
        } catch (Exception e) {
            // Log any exceptions that occur during invoice creation
            logger.error("Error creating invoice: {}", e.getMessage(), e);
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }
    
    */
    /* 
    public Invoice createInvoiceForCalls(Invoice invoice) {
        try {
            // Extract the call IDs from the provided invoice
            Set<Call> callIds = invoice.getCalls();
            logger.info("Call IDs from invoice in createInvoiceForCalls: {}", callIds);

            // Check if callIds exist
            if (callIds == null || callIds.isEmpty()) {
                throw new IllegalArgumentException("Call IDs must not be null or empty.");
            }

            // Fetch the Call objects corresponding to the call IDs
            Set<Call> calls = new HashSet<>();
            for (Call callId : callIds) {
                //Optional<Call> callOptional = callRepository(callId);
               // callOptional.ifPresent(calls::add);
            }
            logger.info("Calls fetched from database: {}", calls);

            // Calculate the total amount for the invoice using the CallService
            BigDecimal totalAmount = callService.calculateTotalAmount(calls);
            logger.info("Total amount calculated: {}", totalAmount);

            // Update the invoice details
            invoice.setTotalAmount(totalAmount.toString());
            invoice.setStatus("Invoiced"); // Set the initial status of the invoice
            logger.info("Updated invoice details: {}", invoice);

            // Save the invoice to the database
            invoice = invoiceRepository.save(invoice);
            logger.info("Invoice saved to database: {}", invoice);

            // Update the status of each call to indicate that it has been invoiced
            for (Call call : calls) {
                call.setStatus("Invoiced");
                callRepository.save(call);
                logger.info("Call status updated: {}", call);
            }

            return invoice;
        } catch (Exception e) {
            // Log any exceptions that occur during invoice creation
            logger.error("Error creating invoice: {}", e.getMessage(), e);
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }
*/
    /*
    public Invoice createInvoiceForCalls(Invoice invoice) {
        try {
            // Extract the calls from the provided invoice
            Set<Call> calls = invoice.getCalls();
            logger.info("calls set from invoice in create invoice for calls {}" + calls);
            // Check if calls exist
            if (calls == null || calls.isEmpty()) {
                      throw new IllegalArgumentException("Calls must not be null or empty.");
            }

            // Calculate the total amount for the invoice using the CallService
            BigDecimal totalAmount = callService.calculateTotalAmount(calls);
            System.out.println("Total amount calculated: " + totalAmount);

            // Update the invoice details
            invoice.setTotalAmount(totalAmount.toString());
            invoice.setStatus("Invoiced"); // Set the initial status of the invoice
            System.out.println("Invoice details updated: " + invoice);

            // Save the invoice to the database
            invoice = invoiceRepository.save(invoice);
            System.out.println("Invoice saved to database: " + invoice);

            // Update the status of each call to indicate that it has been invoiced
            for (Call call : calls) {
                call.setStatus("Invoiced");
                callRepository.save(call);
                System.out.println("Call status updated: " + call);
            }

            return invoice;
        } catch (Exception e) {
            // Log any exceptions that occur during invoice creation
            System.err.println("Error creating invoice: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }

    */
    /* 
    public Invoice createInvoiceForCalls(Invoice invoice) {
        try {
            // Calculate the total amount for the invoice using the CallService
            BigDecimal totalAmount = callService.calculateTotalAmount(calls);
            System.out.println("Total amount calculated: " + totalAmount);

            // Create a new invoice
            Invoice invoice = new Invoice();
            invoice.setTotalAmount(totalAmount.toString());
            invoice.setStatus("Invoiced"); // Set the initial status of the invoice
            System.out.println("New invoice created: " + invoice);

            // Save the invoice to the database
            invoice = invoiceRepository.save(invoice);
            System.out.println("Invoice saved to database: " + invoice);

            // Update the status of each call to indicate that it has been invoiced
            for (Call call : calls) {
                call.setStatus("Invoiced");
                callRepository.save(call);
                System.out.println("Call status updated: " + call);
            }

            return invoice;
        } catch (Exception e) {
            // Log any exceptions that occur during invoice creation
            System.err.println("Error creating invoice: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }*/

/* 
    public Invoice createInvoiceForCalls(Set<Call> calls) {
        // Calculate the total amount for the invoice using the CallService
        BigDecimal totalAmount = callService.calculateTotalAmount(calls);

        // Create a new invoice
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(totalAmount.toString());
        invoice.setStatus("Invoiced"); // Set the initial status of the invoice

        // Save the invoice to the database
        invoice = invoiceRepository.save(invoice);

        // Update the status of each call to indicate that it has been invoiced
        for (Call call : calls) {
            call.setStatus("Invoiced");
            callRepository.save(call);
        }

        return invoice;
    }
    
    */
/* 
    public void generateInvoiceForCall(Call call) {
        // Logic to generate invoice
        call.setStatus("Invoiced");
        callRepository.save(call);
    }*/

    public void processPaymentForInvoice(Invoice invoice) {
        // Logic to process payment for the invoice
       // invoice.setPaid(true); // Mark the invoice as paid
        invoiceRepository.save(invoice); // Save the updated invoice
    }

    /* 

    public List<Call> getAllPaidCalls() {
        List<Call> paidCalls = new ArrayList<>();
        List<Invoice> paidInvoices = invoiceRepository.findByIsPaidTrue();

        for (Invoice invoice : paidInvoices) {
            for (Call call : invoice.getCalls()) {
                if (call.isPaid()) { // Check the payment status of the call
                    paidCalls.add(call);
                }
            }
        }
        return paidCalls;
    }

    public List<Call> getAllUnpaidCalls() {
        List<Call> unpaidCalls = new ArrayList<>();
        List<Invoice> unpaidInvoices = invoiceRepository.findByIsPaidFalse();

        for (Invoice invoice : unpaidInvoices) {
            for (Call call : invoice.getCalls()) {
                if (!call.isPaid()) { // Check the payment status of the call
                    unpaidCalls.add(call);
                }
            }
        }
        logger.info("Unpaid Calls: {}", unpaidCalls);
        return unpaidCalls;
    }

    */

    /* 
    public List<Call> getAllPaidCalls() {
        List<Invoice> paidInvoices = invoiceRepository.findByIsPaidTrue();
        List<Call> paidCalls = new ArrayList<>();
        for (Invoice invoice : paidInvoices) {
            paidCalls.addAll(invoice.getCalls());
        }
        return paidCalls;
    }

    public List<Call> getAllUnpaidCalls() {
        List<Invoice> unpaidInvoices = invoiceRepository.findByIsPaidFalse();
        List<Call> unpaidCalls = new ArrayList<>();
        for (Invoice invoice : unpaidInvoices) {
            unpaidCalls.addAll(invoice.getCalls());
        }
        return unpaidCalls;
    }
*/
/* 
public void triggerInvoiceCreation(String username, InvoiceWithCallIdsDTO invoiceWithCallIdsDTO) {
    // Log that the method is triggered
    logger.info("Triggering invoice creation for user: {}", username);

    List<Call> calls = callRepository.findByUserUsername(username);
    if (calls.size() >= 5) {
        // Check if the call IDs have already been included in previous invoices
        List<Long> callIdsIncludedInPreviousInvoices = callRepository.findCallIdsIncludedInInvoice(username);
        List<Call> callsToAddToInvoice = new ArrayList<>();
        for (Call call : calls) {
            if (!callIdsIncludedInPreviousInvoices.contains(call.getCallId())) {
                callsToAddToInvoice.add(call);
            }
        }
        if (!callsToAddToInvoice.isEmpty()) {
            // Create a new invoice and associate it with the calls
            Invoice invoice = new Invoice();
            invoice.setInvoiceDate(invoiceWithCallIdsDTO.getInvoiceDate()); // Provide the appropriate invoice date
            invoice.setAmount(invoiceWithCallIdsDTO.getAmount());
            //invoice.setPaid(invoiceWithCallIdsDTO.isPaid()); // Initially set as unpaid
            // invoice.setCalls(new HashSet<>(callsToAddToInvoice));
            invoiceRepository.save(invoice); // Save the invoice to the database

            // Log that the invoice is created
            logger.info("Invoice created for user: {}. Number of calls included: {}", username,
                    callsToAddToInvoice.size());
        }
    }
}
    
*/
    //public List<Invoice> getAllInvoices() {
       // return invoiceRepository.findAll();
   // }
/* 
@Override
public void generateInvoiceForCalls(List<Call> calls) {
    // Calculate total cost for the calls
    double totalCost = calculateTotalCost(calls);

    // Create invoice for the call
    Invoice invoice = createInvoice(totalCost);

    // Save or process the invoice as needed
}

*/
/* 
// Helper method to create an invoice for the current call
private Invoice createInvoice(double totalCost) {
    Invoice invoice = new Invoice();
    invoice.setInvoiceDate(LocalDateTime.now().toString());
    invoice.setAmount(String.valueOf(totalCost));
    invoice.setPaid(false); // Assuming the invoice is not paid initially
    //invoice.setInvoice(true); // Marking the invoice as generated
    return invoice;
}
*/

/* 
    @Override
    public List<Invoice> findAllInvoicesWithCallsAndUser() {
       return invoiceRepository.findAllInvoicesWithCallsAndUser();
    }*/
    
}
