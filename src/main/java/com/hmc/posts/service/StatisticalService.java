package com.hmc.posts.service;

import com.hmc.posts.dto.response.PostSendMonthResponse;
import com.hmc.posts.dto.response.PostTotalStatisticalResponse;
import com.hmc.posts.dto.response.UserInfoTotalStatisticalResponse;

import java.util.List;

public interface StatisticalService {

    PostTotalStatisticalResponse totalPost();

    UserInfoTotalStatisticalResponse totalUserInfo();

    List<PostSendMonthResponse> totalPostSendByMonth(Integer year);

}
