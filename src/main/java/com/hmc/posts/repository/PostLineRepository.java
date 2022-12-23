package com.hmc.posts.repository;

import com.hmc.posts.dto.response.PostSendMonthResponse;
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

    @Query("select new PostSendMonthResponse(month(l.timeSend), count(l.id)) from PostLineEntity l" +
            " where l.ownerId = :ownerId and year(l.timeSend) = :year group by month(l.timeSend)")
    List<PostSendMonthResponse> totalPostLineSend(@Param("ownerId") String ownerId, @Param("year") Integer year);

}
