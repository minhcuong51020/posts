package com.hmc.posts.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRedditEntity implements Serializable {

    private String id;

    private String postId;

    private String redditId;

    private String redditNameUrl;

    private String redditName;

    private LocalDate createdAt;

    private Boolean deleted;

}
