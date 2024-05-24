package com.otc.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.otc.backend.models.Call;


@Repository
public interface CallRepository extends JpaRepository<Call, Long> {
  Optional<Call> findByCallId(Long callsId);

  //List<Call> findByPaymentIsNotNull();

  List<Call> findByUserUsername(String username);
  List<Call> findByUser_Username(String username);

    @Query("SELECT c FROM Call c JOIN c.user u WHERE u.username = :username AND c.status = :status")
    List<Call> findByUsernameAndStatus(@Param("username") String username, @Param("status") String status);

//List<Call> findByUsernameAndIsPaidTrue(String username);
//List<Call> findByUser_Username(String username);

  //List<Call> findByUser_UsernameAndIsPaidTrue(Long userId);

  //List<Call> findByUsernameAndIsPaidFalse(String username);

  // Query calls associated with paid invoices for a given user
    //@Query("SELECT DISTINCT c FROM Call c JOIN c.invoices i WHERE i.user.userId = :userId AND i.isPaid = true")
    // List<Call> findPaidCallsByUserId(@Param("userId") Long userId);
   
  //@Query("SELECT DISTINCT c FROM Call c JOIN c.invoices i WHERE i.user.username = :username AND i.isPaid = true")
  //List<Call> findPaidCallsByUsername(@Param("username") String username);

  //List<Call> findByUser_UsernameAndIsPaidTrue(String username);
  //List<Call> findByUser_UsernameAndIsPaidTrue(String username);

  //@Query("SELECT DISTINCT c FROM Call c JOIN c.invoices i WHERE i.user.username = :username AND i.isPaid = true")
  //List<Call> findByUser_UsernameAndIsPaidTrue(String username);

 // @Query("SELECT DISTINCT c FROM Call c JOIN c.invoice i WHERE i.user.username = :username AND i.isPaid = true")
 //List<Call> findByUser_UsernameAndIsPaidTrue(String username);
  
 //List<Call> findByUser_UsernameAndIsPaidTrue(String username);

 //List<Call> findByUserUsernameAndIsPaidTrue(String username);

 //List<Call> findByUserUsernameAndIsPaidFalse(String username);

 //List<Call> findByInvoiceIsPaidTrue();

 //List<Call> findByInvoiceIsPaidFalse();
 
 //List<Call> findByUserUsernameAndIsPaidTrue(String username);
  
  //List<Call> findAllByReceiverUsername(String username);

  //List<Call> findAllCallReceiverByUserName(String username);

  //List<Call> receiver();
	
    //List<Call> findByCallUserUsername(String username);
  
    //List<CallReceiver> findByCallUsersUsername(String username);

    //List<Call> findByReceiverUsername(String username);

   //@Query("SELECT DISTINCT c.callId FROM Invoice i JOIN i.calls c WHERE i.user.username = :username")
  //List<Long> findCallIdsIncludedInInvoice(@Param("username") String username);

   @Query("SELECT DISTINCT c.callId FROM Call c WHERE c.user.username = :username")
    List<Long> findCallIdsIncludedInInvoice(@Param("username") String username);

    //List<Call> findUnpaidCallsByUsername(String username);
  
   // @Query("SELECT c FROM Call c WHERE c.user.username = :username AND c.paid = false")
   //  List<Call> findUnpaidCallsByUsername(@Param("username") String username);
  
  // @Query("SELECT c FROM Call c JOIN c.user u WHERE u.username = :username AND c.paid = false")
  //List<Call> findUnpaidCallsByUsername(@Param("username") String username);
   
  //@Query("SELECT c FROM Call c JOIN c.user u WHERE u.username = :username AND c.isPaid = false")
 // List<Call> findUnpaidCallsByUsername(@Param("username") String username);
}
