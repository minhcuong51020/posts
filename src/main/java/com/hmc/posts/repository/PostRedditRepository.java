package com.hmc.posts.repository;

import com.hmc.posts.dto.response.PostSendMonthResponse;
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

    @Query("select new PostSendMonthResponse(month(l.createdAt), count(l.id)) from PostRedditEntity l" +
            " where l.ownerId = :ownerId and year(l.createdAt) = :year group by month(l.createdAt)")
    List<PostSendMonthResponse> totalPostRedditSend(@Param("ownerId") String ownerId, @Param("year") Integer year);

}
