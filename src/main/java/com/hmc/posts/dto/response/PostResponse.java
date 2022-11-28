package com.hmc.posts.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse implements Serializable {

    private String id;

    private String title;

    private String content;

    private String ownerId;

    private LocalDate createdAt;

    private LocalDate modifiedAt;

    private List<PostRedditResponse> postRedditResponses;

    private Boolean deleted;

}
