package com.hmc.posts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostTwitterRequest implements Serializable {

    private String postId;

    private String twitterId;

}
