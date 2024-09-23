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

    List<Call> findByUserUsername(String username);

    List<Call> findByUser_Username(String username);

    @Query("SELECT c FROM Call c JOIN c.user u WHERE u.username = :username AND c.status = :status")
    List<Call> findByUsernameAndStatus(@Param("username") String username, @Param("status") String status);

    @Query("SELECT DISTINCT c.callId FROM Call c WHERE c.user.username = :username")
    List<Long> findCallIdsIncludedInInvoice(@Param("username") String username);
}
