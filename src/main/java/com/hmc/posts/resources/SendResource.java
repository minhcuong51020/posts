package com.hmc.posts.resources;

import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.PostLineRequest;
import com.hmc.posts.dto.request.PostRedditCreateRequest;
import com.hmc.posts.dto.request.PostTwitterRequest;
import com.hmc.posts.dto.request.PostUserInfoRequest;
import com.hmc.posts.dto.response.PostResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/posts")
public interface SendResource {

    @PostMapping("/api/send/reddit")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> sendReddit(@RequestBody PostRedditCreateRequest request);

    @PostMapping("/api/send/email")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> sendEmail(@RequestBody PostUserInfoRequest request);

    @PostMapping("/api/send/sms")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> sendSms(@RequestBody PostUserInfoRequest request);

    @PostMapping("/api/send/line")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<?> sendLine(@RequestBody PostLineRequest request);

    @PostMapping("/api/send/twitter")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<?> sendTwitter(@RequestBody PostTwitterRequest request);

}
