package com.hmc.posts.mapper;

import com.hmc.common.EntityMapper;
import com.hmc.posts.dto.response.EmailResponse;
import com.hmc.posts.entity.EmailEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface EmailMapper extends EntityMapper<EmailResponse, EmailEntity> {
}
