package com.otc.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.otc.backend.models.CallReceiver;
import com.otc.backend.models.Users;

@Repository
public interface CallReceiverRepository extends JpaRepository<CallReceiver, Long> {
	Optional<CallReceiver> findByCallReceiverId(Long callReceiverId);
	
	//List<CallReceiver> findByCallUser(CallUser callUser);
	
	Optional<CallReceiver> findByTelephone(String telephone);

	//List<CallReceiver> findByCallUser_username(String username);

	//List<CallReceiver> findByUserUsername(String username);

	//List<CallReceiver> findByUsername(String username);

	//List<CallReceiver> findByPhoneNumber(String phoneNumber);

	//List<CallReceiver> findByUserUsername(String username);

	//@Query("SELECT DISTINCT cr.telephone FROM CallReceiver cr JOIN cr.user u JOIN u.calls c WHERE u.username = :username")
	//List<String> findDistinctTelephoneByUserUsername(@Param("username") String username);
	
	List<String> findDistinctTelephoneByUser_Username(String username);

	@Query("SELECT DISTINCT c.telephone FROM CallReceiver c WHERE c.user.username = :username")
	List<String> findDistinctTelephoneByUserUsername(@Param("username") String username);
	
	 boolean existsByUserUsernameAndTelephone(String username, String telephone);

	  List<CallReceiver> findByUserUsername(String username);

}

  

