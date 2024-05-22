package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM User u WHERE u.id = :id")
	User findOneForUpdate(@Param("id") Long id);
}
