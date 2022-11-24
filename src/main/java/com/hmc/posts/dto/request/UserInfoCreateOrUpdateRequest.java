package com.hmc.posts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoCreateOrUpdateRequest implements Serializable {

    private String name;

    private String address;

    private String phone;

    private String email;

}
