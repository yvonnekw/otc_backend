package com.otc.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.otc.backend.models.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.otc.backend.models.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i JOIN i.calls c WHERE c IN :calls")
    Optional<Invoice> findInvoiceByCallsIn(@Param("calls") Set<Call> calls);

    List<Invoice> findAll();

    List<Invoice> findByUserUsername(String username);
}
