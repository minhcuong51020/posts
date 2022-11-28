package com.hmc.posts.repository;

import com.hmc.posts.entity.PostRedditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRedditRepository extends JpaRepository<PostRedditEntity, String> {

    @Query("select pr from PostRedditEntity pr where pr.postId = :postId")
    List<PostRedditEntity> findAllByPostId(@Param("postId") String postId);

}
