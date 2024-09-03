package  com.otc.backend.repository;

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
 // @Query("SELECT i FROM Invoice i JOIN FETCH i.calls WHERE i.user.username = :username")
  //List<Invoice> findByUser_Username(@Param("username") String username);
    
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



   //@Query("SELECT DISTINCT c.callId FROM Invoice i JOIN i.calls c WHERE i.user.username = :username")
   //List<Long> findCallIdsIncludedInInvoices(@Param("username") String username);

  //List<Long> findCallIdsIncludedInInvoices(String username);

 // List<Long> findCallIdsIncludedInInvoices(String username);

}
