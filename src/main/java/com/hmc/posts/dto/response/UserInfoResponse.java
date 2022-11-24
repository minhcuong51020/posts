package com.hmc.posts.dto.response;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse implements Serializable {

    private String id;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String ownerId;

    private LocalDate createdAt;

    private LocalDate modifiedAt;

    private Boolean deleted;

}
