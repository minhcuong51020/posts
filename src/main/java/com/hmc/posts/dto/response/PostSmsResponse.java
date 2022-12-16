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
public class PostSmsResponse implements Serializable {

    private String name;

    private String phone;

    private String address;

    private LocalDate timeSend;

}
