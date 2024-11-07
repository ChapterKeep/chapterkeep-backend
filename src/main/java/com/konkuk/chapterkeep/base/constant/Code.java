package com.konkuk.chapterkeep.base.constant;

import com.konkuk.chapterkeep.base.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    /*

    1. 1x2xx : 성공 응답 (200 OK 관련)
	2. 1x4xx : 클라이언트 오류 (400대 오류, 잘못된 요청 등)
	3. 1x5xx : 서버 오류 (500대 오류, 서버 내부 오류 등)
	4. 2x4xx : 인증 오류 (401 Unauthorized)
	5. 3x4xx : 인가 오류 (403 Forbidden)

     */

    // 성공 응답
    OK(10200, HttpStatus.OK, "OK"),
    CREATED(10201, HttpStatus.CREATED, "Resource created successfully"),
    ACCEPTED(10202, HttpStatus.ACCEPTED, "Request accepted but not processed yet"),
    NO_CONTENT(10203, HttpStatus.NO_CONTENT, "Request succeeded, no content returned"),

    // 클라이언트 오류
    BAD_REQUEST(10400, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(11400, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10404, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    CONFLICT(10409, HttpStatus.CONFLICT, "Conflict in data"),
    PAYLOAD_TOO_LARGE(10413, HttpStatus.PAYLOAD_TOO_LARGE, "Payload too large"),
    UNSUPPORTED_MEDIA_TYPE(10415, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),

    // 서버 오류
    INTERNAL_ERROR(10500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(11500, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),
    SERVICE_UNAVAILABLE(10503, HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"),
    GATEWAY_TIMEOUT(10504, HttpStatus.GATEWAY_TIMEOUT, "Gateway timeout"),
    NOT_IMPLEMENTED(10505, HttpStatus.NOT_IMPLEMENTED, "Not implemented"),

    // 인증 오류
    UNAUTHORIZED(20401, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    TOKEN_EXPIRED(21401, HttpStatus.UNAUTHORIZED, "Token expired"),

    // 인가 오류
    FORBIDDEN(30403, HttpStatus.FORBIDDEN, "Access forbidden");

    private final int code;
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

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return Code.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return Code.INTERNAL_ERROR;
                    } else {
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
