package com.hmc.posts.service;

import com.hmc.posts.dto.request.PostRedditCreateRequest;
import com.hmc.posts.dto.request.PostUserInfoRequest;

public interface SendService {

    void postToEmail(PostUserInfoRequest request);

    void postToSms(PostUserInfoRequest request);

    boolean postToReddit(PostRedditCreateRequest request);

}
