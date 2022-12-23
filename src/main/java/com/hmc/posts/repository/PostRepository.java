package com.hmc.posts.repository;

import com.hmc.posts.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {

    @Query("select count(p) from PostEntity p where p.deleted = false and p.ownerId = :ownerId")
    Long totalPostStatistical(@Param("ownerId") String ownerId);

}
