package com.hmc.posts.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRedditResponse implements Serializable {

    private String id;

    private String redditName;

    private String redditUrl;

    private LocalDate timePostReddit;

}
