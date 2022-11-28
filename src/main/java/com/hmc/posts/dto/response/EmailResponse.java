package com.hmc.posts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponse implements Serializable {

    private String id;

    private String email;

    private String password;

    private String ownerId;

    private LocalDate createdAt;

    private LocalDate modifiedAt;

}
