package com.company.repository;

import com.company.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailEntity, Integer> {
    @Query(value = "SELECT * FROM email ORDER BY created_date DESC LIMIT 1", nativeQuery = true)
    Optional<EmailEntity> getLastEmail();
}
