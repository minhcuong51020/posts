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
public class PostEmailResponse implements Serializable {

    private String id;

    private String name;

    private String email;

    private String address;

    private LocalDate timeSend;

}
