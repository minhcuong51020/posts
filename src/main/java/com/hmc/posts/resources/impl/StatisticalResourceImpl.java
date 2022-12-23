package com.hmc.posts.resources.impl;


import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.response.PostSendMonthResponse;
import com.hmc.posts.dto.response.PostTotalStatisticalResponse;
import com.hmc.posts.dto.response.UserInfoTotalStatisticalResponse;
import com.hmc.posts.resources.StatisticalResource;
import com.hmc.posts.service.StatisticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticalResourceImpl implements StatisticalResource {

    private final StatisticalService statisticalService;


    @Override
    public Response<List<PostSendMonthResponse>> totalPostSendYear(Integer year) {
        return Response.of(statisticalService.totalPostSendByMonth(year));
    }

    @Override
    public Response<PostTotalStatisticalResponse> totalPost() {
        return Response.of(statisticalService.totalPost());
    }

    @Override
    public Response<UserInfoTotalStatisticalResponse> totalPostSendYear() {
        return Response.of(statisticalService.totalUserInfo());
    }
}
