package com.hmc.posts.support;

import com.hmc.common.exception.ResponseError;

public enum BadRequestError  implements ResponseError {

    POST_NOT_FOUND(40007001, "Không tìm thấy bài viết"),

    POST_NOT_ACCESS_DENIED(40007002, "Không có quyền truy cập bài viết"),

    USER_INFO_NOT_FOUND(40008001, "Không tìm thấy thông tin người dùng"),

    USER_INFO_ACCESS_DENIED(40008002, "Không có quyền truy cập thông tin người dùng"),

    EMAIL_NOT_FOUND(40008002, "Không tìm thấy email"),

    SEND_EMAIL_ERROR(40008002, "Gửi email thất bại"),
    ;

    private final Integer code;

    private final String message;

    BadRequestError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
