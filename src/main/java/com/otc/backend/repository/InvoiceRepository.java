package  com.otc.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.otc.backend.models.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    //List<Invoice> findByIsPaidTrue();
    
    //List<Invoice> findByIsPaidFalse();

    //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
    //List<Invoice> findAllInvoiceCallsUser();

  // @Query("SELECT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  //List<Invoice> findAllInvoicesWithCallsAndUsers();
    
  //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  //List<Invoice> findAllInvoicesWithCallsAndUser();

  //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  //List<Invoice> findAllInvoiceCallAndUsers();

  //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  //List<Invoice> findAllInvoicesWithCallsAndUser();

  //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  // List<Invoice> findAllInvoicesWithCallsAndUser();
 
  //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  // List<Invoice> findAllInvoicesWithCallsAndUser();
 
  //@Query("SELECT DISTINCT i FROM Invoice i JOIN FETCH i.calls c JOIN FETCH c.user")
  //List<Invoice> findAllInvoicesWithCallsAndUser();

  List<Invoice> findAll();

   //@Query("SELECT DISTINCT c.callId FROM Invoice i JOIN i.calls c WHERE i.user.username = :username")
   //List<Long> findCallIdsIncludedInInvoices(@Param("username") String username);

  //List<Long> findCallIdsIncludedInInvoices(String username);

 // List<Long> findCallIdsIncludedInInvoices(String username);

}
