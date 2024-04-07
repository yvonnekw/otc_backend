package com.otc.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otc.backend.models.Call;
import com.otc.backend.models.Payment;


public interface PaymentService {

    public List<Payment> getAllPayments(); 

    public Payment getPaymentById(Long paymentId);

    public Payment createPayment(Payment payment);

    public Payment updatePayment(Long paymentId, Payment paymentDetails);

    public void deletePayment(Long paymentId);

   // public List<Call> getPaidCallsByUsername(String username); 
}
