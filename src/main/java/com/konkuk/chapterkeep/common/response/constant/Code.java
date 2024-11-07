package com.konkuk.chapterkeep.common.response.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    /*

    1. S000 : 성공 응답
	2. Exxx : 실패 응답

     */

    // 성공 응답
    OK("S000", HttpStatus.OK, "OK"),
    CREATED("E001", HttpStatus.CREATED, "Resource created successfully"),
    ACCEPTED("E002", HttpStatus.ACCEPTED, "Request accepted but not processed yet"),
    NO_CONTENT("E003", HttpStatus.NO_CONTENT, "Request succeeded, no content returned"),

    // 클라이언트 오류
    BAD_REQUEST("E004", HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR("E005", HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND("E006", HttpStatus.NOT_FOUND, "Requested resource is not found"),
    CONFLICT("E007", HttpStatus.CONFLICT, "Conflict in data"),
    PAYLOAD_TOO_LARGE("E008", HttpStatus.PAYLOAD_TOO_LARGE, "Payload too large"),
    UNSUPPORTED_MEDIA_TYPE("E009", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),

    // 서버 오류
    INTERNAL_ERROR("E010", HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR("E011", HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),
    SERVICE_UNAVAILABLE("E012", HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"),
    GATEWAY_TIMEOUT("E013", HttpStatus.GATEWAY_TIMEOUT, "Gateway timeout"),
    NOT_IMPLEMENTED("E014", HttpStatus.NOT_IMPLEMENTED, "Not implemented"),

    // 인증 오류
    UNAUTHORIZED("E015", HttpStatus.UNAUTHORIZED, "User unauthorized"),
    TOKEN_EXPIRED("E016", HttpStatus.UNAUTHORIZED, "Token expired"),

    // 인가 오류
    FORBIDDEN("E017", HttpStatus.FORBIDDEN, "Access forbidden");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
        return this.message + " - " + throwable.getMessage();
    }

    public String getMessage(String customMessage) {
        return Optional.ofNullable(customMessage)
                .filter(Predicate.not(String::isBlank))
                .map(msg -> this.message + " - " + msg)
                .orElse(this.message);
    }
}