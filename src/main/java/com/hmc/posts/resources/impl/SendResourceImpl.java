package com.hmc.posts.resources.impl;

import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.PostRedditCreateRequest;
import com.hmc.posts.dto.request.PostUserInfoRequest;
import com.hmc.posts.dto.response.PostResponse;
import com.hmc.posts.resources.SendResource;
import com.hmc.posts.service.SendService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendResourceImpl implements SendResource {

    private final SendService sendService;

    public SendResourceImpl(SendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public Response<PostResponse> sendReddit(PostRedditCreateRequest request) {
        this.sendService.postToReddit(request);
        return Response.ok();
    }

    @Override
    public Response<PostResponse> sendEmail(PostUserInfoRequest request) {
        this.sendService.postToEmail(request);
        return Response.ok();
    }

    @Override
    public Response<PostResponse> sendSms(PostUserInfoRequest request) {
        this.sendService.postToSms(request);
        return Response.ok();
    }
}
