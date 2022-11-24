package com.hmc.posts.mapper;

import com.hmc.common.EntityMapper;
import com.hmc.posts.dto.response.PostResponse;
import com.hmc.posts.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface PostMapper extends EntityMapper<PostResponse, PostEntity> {
}
