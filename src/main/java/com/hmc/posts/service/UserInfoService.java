package com.hmc.posts.service;

import com.hmc.common.dto.PageDTO;
import com.hmc.posts.dto.request.UserInfoCreateOrUpdateRequest;
import com.hmc.posts.dto.request.UserInfoSearchRequest;
import com.hmc.posts.dto.response.UserInfoResponse;

public interface UserInfoService {

    UserInfoResponse create(UserInfoCreateOrUpdateRequest request);

    UserInfoResponse update(String id, UserInfoCreateOrUpdateRequest request);

    UserInfoResponse findById(String id);

    UserInfoResponse delete(String id);

    PageDTO<UserInfoResponse> search(UserInfoSearchRequest request);
}
