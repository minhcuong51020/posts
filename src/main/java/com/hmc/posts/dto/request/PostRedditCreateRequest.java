package com.hmc.posts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostRedditCreateRequest implements Serializable {

    private String postId;

    private String redditId;

    private List<String> redditIds;

    private List<String> redditNameUrls;

}
