package com.hmc.posts.repository;

import com.hmc.posts.dto.response.PostSendMonthResponse;
import com.hmc.posts.entity.PostTwitterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTwitterRepository extends JpaRepository<PostTwitterEntity, String> {

    @Query("select T from PostTwitterEntity T where T.postId = :postId")
    List<PostTwitterEntity> findAllByPostId(@Param("postId") String postId);

    @Query("select new PostSendMonthResponse(month(l.timeSend), count(l.id)) from PostTwitterEntity l " +
            "where l.ownerId = :ownerId and year(l.timeSend) = :year group by month(l.timeSend)")
    List<PostSendMonthResponse> totalPostTwitterSend(@Param("ownerId") String ownerId, @Param("year") Integer year);

}
