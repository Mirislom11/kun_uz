package com.company.repository;

import com.company.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>, JpaSpecificationExecutor<ProfileEntity> {

    Optional<ProfileEntity> findByLoginAndPassword(String login, String password);
    Optional<ProfileEntity> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("DELETE FROM ProfileEntity p WHERE p.email = :email")
    void deleteByEmail(@Param("email") String email);
    Optional<ProfileEntity> findByLogin(String login);
}
