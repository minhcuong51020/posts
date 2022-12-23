package com.hmc.posts.repository;

import com.hmc.posts.dto.response.PostSendMonthResponse;
import com.hmc.posts.entity.PostUserInfoEntity;
import com.hmc.posts.support.enums.TypePostUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostUserInfoRepository extends JpaRepository<PostUserInfoEntity, String> {

    @Query("select p from PostUserInfoEntity p where p.postId = :postId and p.type = :type")
    List<PostUserInfoEntity> findAllByPostIdAndType(@Param("postId") String postId,
                                                    @Param("type")TypePostUserInfo type);

    @Query("select new PostSendMonthResponse(month(l.timeSend), count(l.id)) from PostUserInfoEntity l" +
            " where l.ownerId = :ownerId and year(l.timeSend) = :year group by month(l.timeSend)")
    List<PostSendMonthResponse> totalPostUserSend(@Param("ownerId") String ownerId, @Param("year") Integer year);

}
