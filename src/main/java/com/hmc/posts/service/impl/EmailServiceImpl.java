package com.hmc.posts.service.impl;

import com.hmc.common.dto.PageDTO;
import com.hmc.common.enums.error.AuthenticationError;
import com.hmc.common.exception.ResponseException;
import com.hmc.common.util.IdUtils;
import com.hmc.config.SecurityUtils;
import com.hmc.posts.dto.request.EmailCreateOrUpdateRequest;
import com.hmc.posts.dto.request.EmailSearchRequest;
import com.hmc.posts.dto.response.EmailResponse;
import com.hmc.posts.dto.response.PostResponse;
import com.hmc.posts.entity.EmailEntity;
import com.hmc.posts.entity.PostEntity;
import com.hmc.posts.mapper.EmailMapper;
import com.hmc.posts.repository.EmailRepository;
import com.hmc.posts.repository.custom.EmailRepositoryCustom;
import com.hmc.posts.service.EmailService;
import com.hmc.posts.support.BadRequestError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    private final EmailRepositoryCustom emailRepositoryCustom;

    private final EmailMapper emailMapper;

    @Override
    public EmailResponse create(EmailCreateOrUpdateRequest request) {
        String ownerId = ensureUserIdLogin();
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setId(IdUtils.nextId());
        emailEntity.setEmail(request.getEmail());
        emailEntity.setPassword(request.getPassword());
        emailEntity.setCreatedAt(LocalDate.now());
        emailEntity.setOwnerId(ownerId);
        emailEntity.setDeleted(Boolean.FALSE);
        this.emailRepository.save(emailEntity);
        return this.emailMapper.toDomain(emailEntity);
    }

    @Override
    public EmailResponse update(String id, EmailCreateOrUpdateRequest request) {
        EmailEntity emailEntity = ensureEmailEntity(id);
        emailEntity.setEmail(request.getEmail());
        emailEntity.setPassword(request.getPassword());
        emailEntity.setModifiedAt(LocalDate.now());
        this.emailRepository.save(emailEntity);
        return this.emailMapper.toDomain(emailEntity);
    }

    @Override
    public EmailResponse findById(String id) {
        EmailEntity emailEntity = this.ensureEmailEntity(id);
        return this.emailMapper.toDomain(emailEntity);
    }

    @Override
    public EmailResponse delete(String id) {
        EmailEntity emailEntity = this.ensureEmailEntity(id);
        emailEntity.setDeleted(Boolean.TRUE);
        this.emailRepository.save(emailEntity);
        return this.emailMapper.toDomain(emailEntity);
    }

    @Override
    public PageDTO<EmailResponse> search(EmailSearchRequest request) {
        String currentUserId = ensureUserIdLogin();
        List<EmailEntity> emailEntities = this.emailRepositoryCustom.search(
                request, currentUserId
        );
        Long total = this.emailRepositoryCustom.count(request, currentUserId);
        if(total <= 0) {
            return new PageDTO<>();
        }
        List<EmailResponse> emailResponses = this.emailMapper.toDomain(emailEntities);
        return new PageDTO<>(
                emailResponses,
                request.getPageIndex(),
                request.getPageSize(),
                total
        );
    }

    private EmailEntity ensureEmailEntity(String id) {
        return this.emailRepository.findById(id).orElseThrow(
                () -> new ResponseException(BadRequestError.EMAIL_NOT_FOUND)
        );
    }

    private String ensureUserIdLogin() {
        return SecurityUtils.getCurrentUserLoginId().orElseThrow(
                () -> new ResponseException(AuthenticationError.AUTHENTICATION_ERROR)
        );
    }
}
