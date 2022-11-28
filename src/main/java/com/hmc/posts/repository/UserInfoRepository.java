package com.hmc.posts.repository;

import com.hmc.posts.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {

    @Query("select ui from UserInfoEntity ui where ui.deleted = false and ui.id in :ids")
    List<UserInfoEntity> findAllByIds(@Param("ids") List<String> ids);

    @Query("select count(ui) from UserInfoEntity ui where ui.deleted = false and ui.ownerId = :ownerId")
    Long countUserInfo(@Param("ownerId") String ownerId);

}
