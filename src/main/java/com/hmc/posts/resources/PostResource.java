package com.hmc.posts.resources;

import com.hmc.common.dto.response.PagingResponse;
import com.hmc.common.dto.response.Response;
import com.hmc.common.validator.PagingValidator;
import com.hmc.common.validator.ValidatePaging;
import com.hmc.posts.dto.request.PostCreateRequest;
import com.hmc.posts.dto.request.PostSearchRequest;
import com.hmc.posts.dto.request.PostUpdateRequest;
import com.hmc.posts.dto.response.FileResponse;
import com.hmc.posts.dto.response.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/posts")
public interface PostResource {

    @PostMapping("/api/posts")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> createPost(@RequestBody PostCreateRequest request);

    @PostMapping("/api/posts/{id}/update")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> updatePost(@PathVariable("id") String id, @RequestBody PostUpdateRequest request);

    @GetMapping("/api/posts/{id}/detail")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> findPostById(@PathVariable("id") String id);

    @GetMapping("/api/posts/{id}/delete")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<PostResponse> deletePost(@PathVariable("id") String id);

    @GetMapping("/api/posts")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    PagingResponse<PostResponse> searchPost(@ValidatePaging(allowedSorts = {"name", "createdAt"})
                                            PostSearchRequest request);

    @PostMapping("/api/posts/upload")
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    Response<FileResponse> uploadFile(@RequestParam("file") MultipartFile file);
}
