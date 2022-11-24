package com.hmc.posts.resources;

import com.hmc.common.dto.response.PagingResponse;
import com.hmc.common.dto.response.Response;
import com.hmc.common.validator.ValidatePaging;
import com.hmc.posts.dto.request.UserInfoCreateOrUpdateRequest;
import com.hmc.posts.dto.request.UserInfoSearchRequest;
import com.hmc.posts.dto.response.UserInfoResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
public interface UserInfoResource {

    @PostMapping("/api/user-info")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<UserInfoResponse> createUserInfo(@RequestBody UserInfoCreateOrUpdateRequest request);

    @PostMapping("/api/user-info/{id}/update")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<UserInfoResponse> updateUserInfo(@PathVariable("id") String id,
                                              @RequestBody UserInfoCreateOrUpdateRequest request);

    @GetMapping("/api/user-info/{id}/detail")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<UserInfoResponse> detailUserInfo(@PathVariable("id") String id);

    @GetMapping("/api/user-info/{id}/delete")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<UserInfoResponse> deleteUserInfo(@PathVariable("id") String id);

    @GetMapping("/api/user-info")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    PagingResponse<UserInfoResponse> searchUserInfo(@ValidatePaging(allowedSorts = {"name", "createdAt"})
                                                    UserInfoSearchRequest request);
}
