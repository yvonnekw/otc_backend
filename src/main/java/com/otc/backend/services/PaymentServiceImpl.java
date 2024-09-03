package com.otc.backend.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import com.otc.backend.models.Users;
import com.otc.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.otc.backend.dto.PaymentDto;
import com.otc.backend.models.Call;
import com.otc.backend.models.Invoice;
import com.otc.backend.models.Payment;
import com.otc.backend.repository.CallRepository;
import com.otc.backend.repository.InvoiceRepository;
import com.otc.backend.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    
    private final PaymentRepository paymentRepository;
    private final CallRepository callRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CallRepository callRepository, InvoiceRepository invoiceRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.callRepository = callRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Long paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        return paymentOptional.orElse(null); // Return null if payment is not found
    }

    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        try {
            Long invoiceId = paymentDto.getInvoiceId();

            // Check if the invoice exists
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID - from create payment: " + invoiceId));

            logger.info("Invoice to pay - from create payment: {}", invoice);

            // Check if the invoice is already paid
            if ("Paid".equalsIgnoreCase(invoice.getStatus())) {
                throw new IllegalArgumentException("Invoice with ID " + invoiceId + " is already paid.");
            }

            // Retrieve the user associated with the invoice
            Users user = invoice.getUser();
            if (user == null) {
                throw new IllegalArgumentException("No user associated with this invoice - from create payment: " + invoiceId);
            }

            Set<Call> calls = invoice.getCalls();
            logger.info("Calls contained in invoice - from create payment: {}", calls);

            // Update the status of each call to "Paid"
            for (Call call : calls) {
                call.setStatus("Paid");
                callRepository.save(call);
                logger.info("Call status updated - from create payment: {}", call);
            }

            // Set payment details
            payment.setAmount(invoice.getTotalAmount());
            payment.setPaymentDate(paymentDto.getPaymentDate());
            payment.setFullNameOnPaymentCard(paymentDto.getFullNameOnPaymentCard());
            payment.setCardNumber(paymentDto.getCardNumber());
            payment.setExpiringDate(paymentDto.getExpiringDate());
            payment.setIssueNumber(paymentDto.getIssueNumber());
            payment.setSecurityNumber(paymentDto.getSecurityNumber());
            payment.setStatus("Paid");

            // Associate the payment with the invoice and user
            payment.setInvoice(invoice);
            payment.setUser(user);

            // Update invoice status to "Paid"
            invoice.setStatus("Paid");
            logger.info("Updated Invoice to paid - in create payment: {}", invoice);

            // Save the payment
            Payment savedPayment = paymentRepository.save(payment);

            // Update paymentDto with saved payment details
            paymentDto.setPaymentId(savedPayment.getPaymentId());
            paymentDto.setInvoiceId(invoice.getInvoiceId());

            return paymentDto;
        } catch (Exception e) {
            logger.error("Error creating payment - from create payment: {}", e.getMessage(), e);
            throw e;
        }
    }


    public List<PaymentDto> getPaymentsByUsername(String username) {
        // Ensure the user exists before fetching the payments
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        List<Payment> payments = paymentRepository.findByUserUsername(username);

        // Convert the list of Payment entities to a list of PaymentDto
        return payments.stream()
                .map(this::convertToPaymentDto)
                .collect(Collectors.toList());
    }

    private PaymentDto convertToPaymentDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setFullNameOnPaymentCard(payment.getFullNameOnPaymentCard());
        dto.setCardNumber(payment.getCardNumber());
        dto.setExpiringDate(payment.getExpiringDate());
        dto.setIssueNumber(payment.getIssueNumber());
        dto.setSecurityNumber(payment.getSecurityNumber());
        dto.setStatus(payment.getStatus());
        dto.setInvoiceId(payment.getInvoice().getInvoiceId());
        dto.setUsername(payment.getUser().getUsername());
        return dto;
    }

    /*
    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        try {
            Long invoiceId = paymentDto.getInvoiceId();
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID - from create payment: " + invoiceId));

            logger.info("Invoice to pay - from create payment: {}", invoice);

            Set<Call> calls = invoice.getCalls();
            logger.info("Calls contained in invoice - from create payment : {}", calls);

            for (Call call : calls) {
                call.setStatus("Paid");
                callRepository.save(call);
                logger.info("Call status updated - from create payment: {}", call);
            }
            payment.setAmount(invoice.getTotalAmount());
            payment.setPaymentDate(paymentDto.getPaymentDate());
            payment.setFullNameOnPaymentCard(paymentDto.getFullNameOnPaymentCard());
            payment.setCardNumber(paymentDto.getCardNumber());
            payment.setExpiringDate(paymentDto.getExpiringDate());
            payment.setIssueNumber(paymentDto.getIssueNumber());
            payment.setSecurityNumber(paymentDto.getSecurityNumber());
            payment.setStatus("Paid"); 

            payment.setInvoice(invoice);
            invoiceRepository.save(invoice);

            //new to fix payment tests
            paymentDto.setInvoiceId(invoice.getInvoiceId());

            invoice.setStatus("Paid");
            logger.info("Updated Invoice to paid - in create payment: {}", invoice);
            
            Payment savedPayment = paymentRepository.save(payment);
            paymentDto.setPaymentId(savedPayment.getPaymentId());

            return paymentDto;
        } catch (Exception e) {
    
            logger.error("Error creating payment - from create payment: {}", e.getMessage(), e);
            throw e; 
        }
    }

*/
    /* 
    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        try {
            Long invoiceId = paymentDto.getInvoiceId();
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

            logger.info("Invoice to pay: {}", invoice);

            Set<Call> calls = invoice.getCalls();
            logger.info("Calls contained in invoice: {}", calls);

            // Update the status of each call to "Paid" and save them
            for (Call call : calls) {
                call.setStatus("Paid");
                callRepository.save(call);
                logger.info("Call status updated: {}", call);
            }
            payment.setAmount(invoice.getTotalAmount());

            logger.info("Total amount from invoice: {}", payment.getAmount());

            // Set invoice status to "Paid"
            invoice.setStatus("Paid");

            // Save the updated invoice
            invoiceRepository.save(invoice);
            logger.info("Updated Invoice: {}", invoice);

            // Save the payment
            //return paymentRepository.save(payment);
            return paymentDto;
        } catch (Exception e) {
            // Log and handle any exceptions
            logger.error("Error creating payment: {}", e.getMessage(), e);
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }
*/
    

    /* 
    @Transactional
    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        try {
            Invoice invoice = paymentDto.getInvoice();
            logger.info("invoice to pay: {}" + invoice);
            
            Set<Call> calls = invoice.getCalls();
            logger.info("Calls contained in invoice: {}", calls);
            
            // Update the status of each call to "Paid" and save them
            for (Call call : calls) {
                call.setStatus("Paid");
                callRepository.save(call);
                logger.info("Call status updated: {}", call);
            }
            payment.setAmount(invoice.getTotalAmount());
            
            logger.info("Total amount from invoice: {}", payment.getAmount());
            
            // Set invoice status to "Paid"
            invoice.setStatus("Paid");
            
            // Save the updated invoice
            invoiceRepository.save(invoice);
            logger.info("Updated Invoice: {}", invoice);

            // Save the payment
            //return paymentRepository.save(payment);
            return paymentDto;
        } catch (Exception e) {
            // Log and handle any exceptions
            logger.error("Error creating payment: {}", e.getMessage(), e);
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }
*/
    /* 
    @Override
    public Payment createPayment(Payment payment) {
      
        Invoice invoice = payment.getInvoice();
        Set<Call> calls = invoice.getCalls();
         invoice.setStatus("Paid"); // Set invoice as paid

        // Iterate through the calls and update their isPaid status
        ///for (Call call : invoice.getCalls()) {
            //call.setStatus("Paid"); // Set call as paid
       // }
         
        // Update the status of each call to indicate that it has been invoiced
        for (Call call : calls) {
            call.setStatus("Paid");
            callRepository.save(call);
            logger.info("Call status updated: {}", call);
        }

        // Save the invoice and associated calls
        invoiceRepository.save(invoice);

        // Log the updated invoice and associated calls for debugging
        logger.info("Updated Invoice: {}", invoice);
        // logger.info("Updated Calls: {}", invoice.getCalls());

        return paymentRepository.save(payment);
    }
    */

/* 
    @Override
    public Payment createPayment(Payment payment) {
        Invoice invoice = payment.getInvoice();
        invoice.setPaid(true);
        invoiceRepository.save(invoice);

        Set<Call> calls = invoice.getCalls();

        // Iterate through the calls and update their isPaid status
        for (Call call : calls) {
            call.setPaid(true);
            callRepository.save(call);
            logger.info("calls being updaed with payment : {}", call);
        }

        return paymentRepository.save(payment);
    }
*/
    /* 
    @Override
    public Payment createPayment(Payment payment) {
        Invoice invoice = payment.getInvoice();
        invoice.setPaid(true);
        invoiceRepository.save(invoice);
        return paymentRepository.save(payment);
    }
*/
    @Override
    public Payment updatePayment(Long paymentId, Payment paymentDetails) {
        // Check if the payment exists
        Payment existingPayment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        // Update the existing payment with the details provided
        existingPayment.setAmount(paymentDetails.getAmount());
        // Update other fields as needed

        // Save and return the updated payment
        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        // Check if the payment exists
        Payment existingPayment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        // Delete the payment
        paymentRepository.delete(existingPayment);
    }

    /* 
    @Override
    public List<Call> getPaidCallsByUsername(String username) {

        return callRepository.findByUser_UsernameAndIsPaidTrue(username);
    }
    */
}

