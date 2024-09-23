package com.otc.backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import com.otc.backend.models.Users;
import com.otc.backend.repository.UserRepository;
import com.otc.backend.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

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

            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID - from create payment: " + invoiceId));

            logger.info("Invoice to pay - from create payment: {}", invoice);

            if ("Paid".equalsIgnoreCase(invoice.getStatus())) {
                throw new IllegalArgumentException("Invoice with ID " + invoiceId + " is already paid.");
            }

            Users user = invoice.getUser();
            if (user == null) {
                throw new IllegalArgumentException("No user associated with this invoice - from create payment: " + invoiceId);
            }

            Set<Call> calls = invoice.getCalls();
            logger.info("Calls contained in invoice - from create payment: {}", calls);

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
            payment.setUser(user);

            invoice.setStatus("Paid");
            logger.info("Updated Invoice to paid - in create payment: {}", invoice);

            Payment savedPayment = paymentRepository.save(payment);

            paymentDto.setPaymentId(savedPayment.getPaymentId());
            paymentDto.setInvoiceId(invoice.getInvoiceId());

            return paymentDto;
        } catch (Exception e) {
            logger.error("Error creating payment - from create payment: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<PaymentDto> getPaymentsByUsername(String username) {

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        List<Payment> payments = paymentRepository.findByUserUsername(username);

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

    @Override
    public Payment updatePayment(Long paymentId, Payment paymentDetails) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        existingPayment.setAmount(paymentDetails.getAmount());

        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        paymentRepository.delete(existingPayment);
    }
}

