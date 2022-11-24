package com.hmc.posts.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hmc.posts.dto.response.FileResponse;
import com.hmc.posts.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Cloudinary cloudinary;

    public FileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public FileResponse fileResponse(MultipartFile multipartFile) {
        String thumbnail = "";
        if(!multipartFile.isEmpty()) {
            try {
                Map r = this.cloudinary.uploader().upload(multipartFile.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                thumbnail = (String) r.get("secure_url");
            } catch (IOException e) {
                log.error("Can't upload file");
            }
        }
        return new FileResponse(thumbnail);
    }
}
