package com.hmc.posts.service;

import com.hmc.common.dto.PageDTO;
import com.hmc.posts.dto.request.PostCreateRequest;
import com.hmc.posts.dto.request.PostSearchRequest;
import com.hmc.posts.dto.request.PostUpdateRequest;
import com.hmc.posts.dto.response.PostResponse;

public interface PostService {

    PostResponse create(PostCreateRequest request);

    PostResponse update(String id, PostUpdateRequest request);

    PostResponse findById(String id);

    PageDTO<PostResponse> search(PostSearchRequest request);

    PostResponse delete(String id);

}
