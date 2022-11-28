package com.hmc.posts.repository;

import com.hmc.posts.entity.PostUserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostUserInfoRepository extends JpaRepository<PostUserInfoEntity, String> {

}
