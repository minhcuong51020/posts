package com.hmc.posts.dto.response;

import com.hmc.posts.support.enums.TypePostSocial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSocialResponse implements Serializable {

    private TypePostSocial typePostSocial;

    private String accountName;

    private LocalDate timeSend;

}
