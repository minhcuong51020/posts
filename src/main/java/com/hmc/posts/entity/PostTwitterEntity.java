package com.hmc.posts.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "post_twitter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostTwitterEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "twitter_id", nullable = false)
    private String twitterId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "time_send", nullable = false)
    private LocalDate timeSend;

}
