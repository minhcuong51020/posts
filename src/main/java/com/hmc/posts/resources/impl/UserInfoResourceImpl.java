package com.hmc.posts.resources.impl;

import com.hmc.common.dto.response.PagingResponse;
import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.UserInfoCreateOrUpdateRequest;
import com.hmc.posts.dto.request.UserInfoSearchRequest;
import com.hmc.posts.dto.response.UserInfoResponse;
import com.hmc.posts.resources.UserInfoResource;
import com.hmc.posts.service.UserInfoService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoResourceImpl implements UserInfoResource {

    private final UserInfoService userInfoService;

    public UserInfoResourceImpl(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public Response<UserInfoResponse> createUserInfo(UserInfoCreateOrUpdateRequest request) {
        return Response.of(this.userInfoService.create(request));
    }

    @Override
    public Response<UserInfoResponse> updateUserInfo(String id, UserInfoCreateOrUpdateRequest request) {
        return Response.of(this.userInfoService.update(id, request));
    }

    @Override
    public Response<UserInfoResponse> detailUserInfo(String id) {
        return Response.of(this.userInfoService.findById(id));
    }

    @Override
    public Response<UserInfoResponse> deleteUserInfo(String id) {
        return Response.of(this.userInfoService.delete(id));
    }

    @Override
    public PagingResponse<UserInfoResponse> searchUserInfo(UserInfoSearchRequest request) {
        return PagingResponse.of(this.userInfoService.search(request));
    }

    @Override
    public PagingResponse<UserInfoResponse> searchUserInfoAuto(UserInfoSearchRequest request) {
        return PagingResponse.of(this.userInfoService.searchAuto(request));
    }
}
