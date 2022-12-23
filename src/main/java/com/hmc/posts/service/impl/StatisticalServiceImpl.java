package com.hmc.posts.service.impl;

import com.hmc.common.enums.error.AuthenticationError;
import com.hmc.common.exception.ResponseException;
import com.hmc.config.SecurityUtils;
import com.hmc.posts.dto.response.PostSendMonthResponse;
import com.hmc.posts.dto.response.PostTotalStatisticalResponse;
import com.hmc.posts.dto.response.UserInfoTotalStatisticalResponse;
import com.hmc.posts.entity.PostRedditEntity;
import com.hmc.posts.repository.*;
import com.hmc.posts.service.StatisticalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatisticalServiceImpl implements StatisticalService {

    private final PostRepository postRepository;

    private final UserInfoRepository userInfoRepository;

    private final PostLineRepository postLineRepository;

    private final PostTwitterRepository postTwitterRepository;

    private final PostRedditRepository postRedditRepository;

    private final PostUserInfoRepository postUserInfoRepository;

    @Override
    public PostTotalStatisticalResponse totalPost() {
        String ownerId = ensureUserIdLogin();
        Long total = postRepository.totalPostStatistical(ownerId);
        PostTotalStatisticalResponse postTotalStatisticalResponse = new PostTotalStatisticalResponse();
        if(Objects.nonNull(total)) {
            postTotalStatisticalResponse.setTotal(total);
        } else {
            postTotalStatisticalResponse.setTotal(0);
        }
        return postTotalStatisticalResponse;
    }

    @Override
    public UserInfoTotalStatisticalResponse totalUserInfo() {
        String ownerId = ensureUserIdLogin();
        Long total = userInfoRepository.countUserInfo(ownerId);
        UserInfoTotalStatisticalResponse response = new UserInfoTotalStatisticalResponse();
        if(Objects.nonNull(response)) {
            response.setTotal(total);
        } else {
            response.setTotal(0);
        }
        return response;
    }

    @Override
    public List<PostSendMonthResponse> totalPostSendByMonth(Integer year) {
        String ownerId = ensureUserIdLogin();
        List<PostSendMonthResponse> postLineSend = this.postLineRepository.totalPostLineSend(ownerId, year);
        List<PostSendMonthResponse> postTwitterSend = this.postTwitterRepository.totalPostTwitterSend(ownerId, year);
        List<PostSendMonthResponse> postRedditSend = this.postRedditRepository.totalPostRedditSend(ownerId, year);
        List<PostSendMonthResponse> postUserSend = this.postUserInfoRepository.totalPostUserSend(ownerId, year);
        List<PostSendMonthResponse> responses = new ArrayList<>();
        for(int i = 1; i <= 12; i++) {
            responses.add(new PostSendMonthResponse(i, 0));
        }
        if(Objects.nonNull(postLineSend)) {
            postLineSend.forEach((item) -> {
                PostSendMonthResponse response = responses.get(item.getMonth() - 1);
                responses.get(item.getMonth() - 1).setTotal(response.getTotal() + item.getTotal());
            });
        }
        if(Objects.nonNull(postTwitterSend)) {
            postTwitterSend.forEach((item) -> {
                PostSendMonthResponse response = responses.get(item.getMonth() - 1);
                responses.get(item.getMonth() - 1).setTotal(response.getTotal() + item.getTotal());
            });
        }
        if(Objects.nonNull(postRedditSend)) {
            postRedditSend.forEach((item) -> {
                PostSendMonthResponse response = responses.get(item.getMonth() - 1);
                responses.get(item.getMonth() - 1).setTotal(response.getTotal() + item.getTotal());
            });
        }
        if(Objects.nonNull(postUserSend)) {
            postUserSend.forEach((item) -> {
                PostSendMonthResponse response = responses.get(item.getMonth() - 1);
                responses.get(item.getMonth() - 1).setTotal(response.getTotal() + item.getTotal());
            });
        }
        return responses;
    }

    private String ensureUserIdLogin() {
        return SecurityUtils.getCurrentUserLoginId().orElseThrow(
                () -> new ResponseException(AuthenticationError.AUTHENTICATION_ERROR)
        );
    }
}
