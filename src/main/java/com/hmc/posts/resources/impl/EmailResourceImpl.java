package com.hmc.posts.resources.impl;

import com.hmc.common.dto.response.PagingResponse;
import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.EmailCreateOrUpdateRequest;
import com.hmc.posts.dto.request.EmailSearchRequest;
import com.hmc.posts.dto.response.EmailResponse;
import com.hmc.posts.resources.EmailResource;
import com.hmc.posts.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailResourceImpl implements EmailResource {

    private final EmailService emailService;

    @Override
    public Response<EmailResponse> create(EmailCreateOrUpdateRequest request) {
        return Response.of(emailService.create(request));
    }

    @Override
    public Response<EmailResponse> update(String id, EmailCreateOrUpdateRequest request) {
        return Response.of(emailService.update(id, request));
    }

    @Override
    public Response<EmailResponse> findById(String id) {
        return Response.of(emailService.findById(id));
    }

    @Override
    public Response<EmailResponse> delete(String id) {
        return Response.of(emailService.delete(id));
    }

    @Override
    public PagingResponse<EmailResponse> search(EmailSearchRequest request) {
        return PagingResponse.of(this.emailService.search(request));
    }
}
