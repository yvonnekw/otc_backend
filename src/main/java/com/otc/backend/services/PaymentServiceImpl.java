package com.otc.backend.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


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

    public PaymentServiceImpl(PaymentRepository paymentRepository, CallRepository callRepository, InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.callRepository = callRepository;
        this.invoiceRepository = invoiceRepository;
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

