package com.otc.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otc.backend.models.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmailAddress(String emailAddress);

    String findFirstNameByUsername(String username);

    String findLastNameByUsername(String username);

    String findEmailByUsername(String username);
}
