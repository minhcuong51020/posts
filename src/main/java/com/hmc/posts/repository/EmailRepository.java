package com.hmc.posts.repository;

import com.hmc.posts.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailEntity, String> {

    @Query("select e from EmailEntity e where e.deleted = false and e.ownerId = :ownerId")
    Optional<EmailEntity> findByOwnerId(@Param("ownerId") String ownerId);

}
