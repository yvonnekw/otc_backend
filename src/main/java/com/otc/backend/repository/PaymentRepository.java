package com.otc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otc.backend.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

    

    
}
