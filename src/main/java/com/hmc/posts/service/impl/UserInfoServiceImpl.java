package com.hmc.posts.service.impl;

import com.hmc.common.dto.PageDTO;
import com.hmc.common.enums.error.AuthenticationError;
import com.hmc.common.exception.ResponseException;
import com.hmc.common.util.IdUtils;
import com.hmc.config.SecurityUtils;
import com.hmc.posts.dto.request.UserInfoCreateOrUpdateRequest;
import com.hmc.posts.dto.request.UserInfoSearchRequest;
import com.hmc.posts.dto.response.UserInfoResponse;
import com.hmc.posts.entity.UserInfoEntity;
import com.hmc.posts.mapper.UserInfoMapper;
import com.hmc.posts.repository.UserInfoRepository;
import com.hmc.posts.repository.custom.UserInfoRepositoryCustom;
import com.hmc.posts.service.UserInfoService;
import com.hmc.posts.support.BadRequestError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    private final UserInfoRepositoryCustom userInfoRepositoryCustom;

    private final UserInfoMapper userInfoMapper;

    @Override
    public UserInfoResponse create(UserInfoCreateOrUpdateRequest request) {
        String ownerId = ensureUserIdLogin();
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setId(IdUtils.nextId());
        userInfoEntity.setName(request.getName());
        userInfoEntity.setAddress(request.getAddress());
        userInfoEntity.setPhone(request.getPhone());
        userInfoEntity.setEmail(request.getEmail());
        userInfoEntity.setOwnerId(ownerId);
        userInfoEntity.setCreatedAt(LocalDate.now());
        userInfoEntity.setModifiedAt(LocalDate.now());
        userInfoEntity.setDeleted(Boolean.FALSE);
        this.userInfoRepository.save(userInfoEntity);
        return this.userInfoMapper.toDomain(userInfoEntity);
    }

    @Override
    public UserInfoResponse update(String id, UserInfoCreateOrUpdateRequest request) {
        String ownerId = ensureUserIdLogin();
        UserInfoEntity userInfoEntity = ensureUserInfoEntity(id);
        if(!userInfoEntity.getOwnerId().equals(ownerId)) {
            throw new ResponseException(BadRequestError.USER_INFO_ACCESS_DENIED);
        }
        userInfoEntity.setName(request.getName());
        userInfoEntity.setAddress(request.getAddress());
        userInfoEntity.setPhone(request.getPhone());
        userInfoEntity.setEmail(request.getEmail());
        userInfoEntity.setModifiedAt(LocalDate.now());
        this.userInfoRepository.save(userInfoEntity);
        return this.userInfoMapper.toDomain(userInfoEntity);
    }

    @Override
    public UserInfoResponse findById(String id) {
        UserInfoEntity userInfoEntity = ensureUserInfoEntity(id);
        return this.userInfoMapper.toDomain(userInfoEntity);
    }

    @Override
    public UserInfoResponse delete(String id) {
        UserInfoEntity userInfoEntity = this.ensureUserInfoEntity(id);
        userInfoEntity.setDeleted(Boolean.TRUE);
        this.userInfoRepository.save(userInfoEntity);
        return this.userInfoMapper.toDomain(userInfoEntity);
    }

    @Override
    public PageDTO<UserInfoResponse> search(UserInfoSearchRequest request) {
        String currentUserLogin = this.ensureUserIdLogin();
        Long total = this.userInfoRepositoryCustom.count(request, currentUserLogin);
        if(total <= 0) {
            return new PageDTO<>();
        }
        List<UserInfoEntity> userInfoEntities = this.userInfoRepositoryCustom.search(request, currentUserLogin);
        List<UserInfoResponse> userInfoResponses = this.userInfoMapper.toDomain(userInfoEntities);
        return new PageDTO<>(
                userInfoResponses,
                request.getPageIndex(),
                request.getPageSize(),
                total
        );
    }

    @Override
    public PageDTO<UserInfoResponse> searchAuto(UserInfoSearchRequest request) {
        String currentUserLogin = this.ensureUserIdLogin();
        Long total = this.userInfoRepository.countUserInfo(currentUserLogin);
        if(total <= 0) {
            return new PageDTO<>();
        }
        request.setPageSize(Integer.parseInt(total.toString()));
        List<UserInfoEntity> userInfoEntities = this.userInfoRepositoryCustom.search(request, currentUserLogin);
        List<UserInfoResponse> userInfoResponses = this.userInfoMapper.toDomain(userInfoEntities);
        return new PageDTO<>(
                userInfoResponses,
                request.getPageIndex(),
                request.getPageSize(),
                total
        );
    }

    private UserInfoEntity ensureUserInfoEntity(String id) {
        return this.userInfoRepository.findById(id).orElseThrow(
                () -> new ResponseException(BadRequestError.USER_INFO_NOT_FOUND)
        );
    }

    private String ensureUserIdLogin() {
        return SecurityUtils.getCurrentUserLoginId().orElseThrow(
                () -> new ResponseException(AuthenticationError.AUTHENTICATION_ERROR)
        );
    }
}
