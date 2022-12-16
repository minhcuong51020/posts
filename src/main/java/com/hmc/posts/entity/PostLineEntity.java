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
@Table(name = "post_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLineEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "line_id", nullable = false)
    private String lineId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "time_send", nullable = false)
    private LocalDate timeSend;

}
