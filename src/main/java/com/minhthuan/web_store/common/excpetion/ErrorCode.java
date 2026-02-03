package com.minhthuan.web_store.common.excpetion;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
    RATING_INVALID(400, "Rating must be between 1 and 5", HttpStatus.BAD_REQUEST),
    NOT_NULL(404, "Data must not null", HttpStatus.BAD_REQUEST),
    EXISTED(404, "Exist data", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Not Found Data", HttpStatus.NOT_FOUND),
    INCORRECT_EMAIL_PASSWORD(404, "Email or password incorrect", HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTED(404, "Email is existed", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(404, "Token not found", HttpStatus.NOT_FOUND),
    TOKEN_INVALID(400, "Token is expired or revoked", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(400, "Password must be at least 9 character long", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(400, "Phone must be at least 10 character long", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VERIFIED(400, "Email is not verified", HttpStatus.BAD_REQUEST),
    ERROR_CODE_DOMAIN(9999, "Domain error", HttpStatus.INTERNAL_SERVER_ERROR);
    private int code;
    private String message;
    private HttpStatus status;

}
