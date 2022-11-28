package com.hmc.posts.resources.impl;

import com.hmc.client.SocialClient;
import com.hmc.common.dto.response.PagingResponse;
import com.hmc.common.dto.response.RedditDTO;
import com.hmc.common.dto.response.RedditGroupDTO;
import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.PostCreateRequest;
import com.hmc.posts.dto.request.PostSearchRequest;
import com.hmc.posts.dto.request.PostUpdateRequest;
import com.hmc.posts.dto.response.FileResponse;
import com.hmc.posts.dto.response.PostResponse;
import com.hmc.posts.resources.PostResource;
import com.hmc.posts.service.FileService;
import com.hmc.posts.service.PostService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PostResourceImpl implements PostResource {

    private final PostService postService;

    private final FileService fileService;

    private final SocialClient socialClient;

    public PostResourceImpl(PostService postService, FileService fileService, SocialClient socialClient) {
        this.postService = postService;
        this.fileService = fileService;
        this.socialClient = socialClient;
    }

    @Override
    public Response<PostResponse> createPost(PostCreateRequest request) {
        return Response.of(this.postService.create(request));
    }

    @Override
    public Response<PostResponse> updatePost(String id, PostUpdateRequest request) {
        return Response.of(this.postService.update(id, request));
    }

    @Override
    public Response<PostResponse> findPostById(String id) {
        return Response.of(this.postService.findById(id));
    }

    @Override
    public Response<PostResponse> deletePost(String id) {
        return Response.of(this.postService.delete(id));
    }

    @Override
    public PagingResponse<PostResponse> searchPost(PostSearchRequest request) {
        return PagingResponse.of(this.postService.search(request));
    }

    @Override
    public Response<FileResponse> uploadFile(MultipartFile file) {
        FileResponse response = this.fileService.fileResponse(file);
        return Response.of(response);
    }
}
