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

    1. Sxxx : 성공 응답
	2. EXxxx : 실패 응답

     */

    // 성공 응답 (Sxxx)
    OK("S000", HttpStatus.OK, "요청이 성공하였습니다."),

    // 클라이언트 오류 (ECxxx)
    BAD_REQUEST("EC001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND("EC002", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INVALID_INPUT_VALUE("EC003", HttpStatus.BAD_REQUEST, "유효하지 않은 값을 입력하였습니다."),

    // 서버 오류 (ESxxx)
    INTERNAL_ERROR("ES001", HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 서버 에러가 발생했습니다."),

    // 인증,인가 오류 (EUxxx)
    UNAUTHORIZED("EU001", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    TOKEN_EXPIRED("EU002", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    FORBIDDEN("EU003", HttpStatus.FORBIDDEN, "접근이 허용되지 않았습니다.");

    // 비즈니스 오류 (EBxxx)


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