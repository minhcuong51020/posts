package com.hmc.posts.resources;

import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.PostRedditCreateRequest;
import com.hmc.posts.dto.response.PostResponse;
import com.hmc.posts.dto.response.PostSendMonthResponse;
import com.hmc.posts.dto.response.PostTotalStatisticalResponse;
import com.hmc.posts.dto.response.UserInfoTotalStatisticalResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts")
public interface StatisticalResource {

    @PostMapping("/api/statistical/post/{year}/send")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<List<PostSendMonthResponse>> totalPostSendYear(@PathVariable("year") Integer year);

    @GetMapping("/api/statistical/post/total")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostTotalStatisticalResponse> totalPost();

    @GetMapping("/api/statistical/user/total")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<UserInfoTotalStatisticalResponse> totalPostSendYear();
}
