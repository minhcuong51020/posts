package com.hmc.posts.resources;

import com.hmc.common.dto.response.PagingResponse;
import com.hmc.common.dto.response.Response;
import com.hmc.posts.dto.request.EmailCreateOrUpdateRequest;
import com.hmc.posts.dto.request.EmailSearchRequest;
import com.hmc.posts.dto.response.EmailResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
public interface EmailResource {

    @PostMapping("/api/email")
    Response<EmailResponse> create(@RequestBody EmailCreateOrUpdateRequest request);

    @PostMapping("/api/email/{id}/update")
    Response<EmailResponse> update(@PathVariable("id") String id,@RequestBody EmailCreateOrUpdateRequest request);

    @GetMapping("/api/email/{id}/detail")
    Response<EmailResponse> findById(@PathVariable("id") String id);

    @PostMapping("/api/email/{id}/delete")
    Response<EmailResponse> delete(@PathVariable("id") String id);

    @GetMapping("/api/email")
    PagingResponse<EmailResponse> search(EmailSearchRequest request);

}
