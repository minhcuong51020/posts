package com.hmc.posts.dto.request;

import com.hmc.posts.support.enums.TypePostUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserInfoRequest implements Serializable {

    private String postId;

    private List<String> userInfoIds;

    private String emailId;

    private TypePostUserInfo type;

}
