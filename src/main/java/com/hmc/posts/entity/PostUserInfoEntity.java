package com.hmc.posts.entity;

import com.hmc.posts.support.enums.TypePostUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "post_userInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserInfoEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "userInfo_id", nullable = false)
    private String userInfoId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePostUserInfo type;

    @Column(name = "time_send", nullable = false)
    private LocalDate timeSend;

}
