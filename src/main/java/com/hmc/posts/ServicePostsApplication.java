package com.hmc.posts;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hmc.posts.support.CloudinaryCommon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ServicePostsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePostsApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CloudinaryCommon.cloudName,
                "api_key", CloudinaryCommon.apiKey,
                "api_secret", CloudinaryCommon.apiSecret,
                "secure", CloudinaryCommon.secure
        ));
        return c;
    }

}
