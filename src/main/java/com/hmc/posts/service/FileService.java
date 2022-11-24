package com.hmc.posts.service;

import com.hmc.posts.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileResponse fileResponse(MultipartFile multipartFile);

}
