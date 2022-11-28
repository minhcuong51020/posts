package com.hmc.posts.service;

import com.hmc.common.dto.PageDTO;
import com.hmc.posts.dto.request.EmailCreateOrUpdateRequest;
import com.hmc.posts.dto.request.EmailSearchRequest;
import com.hmc.posts.dto.response.EmailResponse;
import com.hmc.posts.entity.EmailEntity;

public interface EmailService {

    EmailResponse create(EmailCreateOrUpdateRequest request);

    EmailResponse update(String id, EmailCreateOrUpdateRequest request);

    EmailResponse findById(String id);

    EmailResponse delete(String id);

    PageDTO<EmailResponse> search(EmailSearchRequest request);

}
