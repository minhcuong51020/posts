package com.hmc.posts.mapper;

import com.hmc.common.EntityMapper;
import com.hmc.posts.dto.response.UserInfoResponse;
import com.hmc.posts.entity.UserInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserInfoMapper extends EntityMapper<UserInfoResponse, UserInfoEntity> {
}
