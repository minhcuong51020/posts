package com.hmc.posts.repository;

import com.hmc.posts.entity.PostLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLineRepository extends JpaRepository<PostLineEntity, String> {

    @Query("select l from PostLineEntity l where l.postId = :postId")
    List<PostLineEntity> findAllByPostId(@Param("postId") String postId);

}
