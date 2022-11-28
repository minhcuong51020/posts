package com.hmc.posts.service.impl;

import com.hmc.client.SocialClient;
import com.hmc.common.dto.PageDTO;
import com.hmc.common.dto.response.RedditDTO;
import com.hmc.common.dto.response.RedditGroupDTO;
import com.hmc.common.enums.error.AuthenticationError;
import com.hmc.common.exception.ResponseException;
import com.hmc.common.util.IdUtils;
import com.hmc.config.SecurityUtils;
import com.hmc.posts.dto.request.PostCreateRequest;
import com.hmc.posts.dto.request.PostSearchRequest;
import com.hmc.posts.dto.request.PostUpdateRequest;
import com.hmc.posts.dto.response.PostRedditResponse;
import com.hmc.posts.dto.response.PostResponse;
import com.hmc.posts.entity.PostEntity;
import com.hmc.posts.entity.PostRedditEntity;
import com.hmc.posts.mapper.PostMapper;
import com.hmc.posts.repository.PostRedditRepository;
import com.hmc.posts.repository.PostRepository;
import com.hmc.posts.repository.custom.PostRepositoryCustom;
import com.hmc.posts.service.PostService;
import com.hmc.posts.support.BadRequestError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostRepositoryCustom postRepositoryCustom;

    private final PostMapper postMapper;

    private final PostRedditRepository postRedditRepository;

    private final SocialClient socialClient;

    @Override
    public PostResponse create(PostCreateRequest request) {
        String ownerId = ensureUserIdLogin();
        PostEntity postEntity = new PostEntity();
        postEntity.setId(IdUtils.nextId());
        postEntity.setTitle(request.getTitle());
        postEntity.setContent(request.getContent());
        postEntity.setOwnerId(ownerId);
        postEntity.setCreatedAt(LocalDate.now());
        postEntity.setModifiedAt(LocalDate.now());
        postEntity.setDeleted(Boolean.FALSE);
        this.postRepository.save(postEntity);
        return this.postMapper.toDomain(postEntity);
    }

    @Override
    public PostResponse update(String id, PostUpdateRequest request) {
        String ownerId = ensureUserIdLogin();
        PostEntity postEntity = ensurePostEntity(id);
        if(!postEntity.getOwnerId().equals(ownerId)) {
            throw new ResponseException(BadRequestError.POST_NOT_ACCESS_DENIED);
        }
        postEntity.setTitle(request.getTitle());
        postEntity.setContent(request.getContent());
        postEntity.setModifiedAt(LocalDate.now());
        this.postRepository.save(postEntity);
        return this.postMapper.toDomain(postEntity);
    }

    @Override
    public PostResponse findById(String id) {
        PostEntity postEntity = ensurePostEntity(id);
        PostResponse postResponse = this.postMapper.toDomain(postEntity);
        List<PostRedditResponse> postRedditResponseList = new ArrayList<>();
        List<PostRedditEntity> postRedditEntities = this.postRedditRepository.findAllByPostId(id);
        for (PostRedditEntity pr : postRedditEntities) {
            RedditGroupDTO group = this.socialClient.getRedditGroupById(pr.getRedditGroupId()).getData();
            PostRedditResponse response = new PostRedditResponse(
                    pr.getId(), group.getName(), group.getNameUrl(), pr.getCreatedAt()
            );
            postRedditResponseList.add(response);
        }
        postResponse.setPostRedditResponses(postRedditResponseList);
        return postResponse;
    }

    @Override
    public PageDTO<PostResponse> search(PostSearchRequest request) {
        String currentUserId = ensureUserIdLogin();
        List<PostEntity> postEntities = this.postRepositoryCustom.search(
                request, currentUserId
        );
        Long total = this.postRepositoryCustom.count(request, currentUserId);
        if(total <= 0) {
            return new PageDTO<>();
        }
        List<PostResponse> postResponses = this.postMapper.toDomain(postEntities);
        return new PageDTO<>(
                postResponses,
                request.getPageIndex(),
                request.getPageSize(),
                total
        );
    }

    @Override
    public PostResponse delete(String id) {
        PostEntity postEntity = this.ensurePostEntity(id);
        postEntity.setDeleted(Boolean.TRUE);
        this.postRepository.save(postEntity);
        return this.postMapper.toDomain(postEntity);
    }

    private PostEntity ensurePostEntity(String id) {
        return this.postRepository.findById(id).orElseThrow(
                () -> new ResponseException(BadRequestError.POST_NOT_FOUND)
        );
    }

    private String ensureUserIdLogin() {
        return SecurityUtils.getCurrentUserLoginId().orElseThrow(
                () -> new ResponseException(AuthenticationError.AUTHENTICATION_ERROR)
        );
    }
}
