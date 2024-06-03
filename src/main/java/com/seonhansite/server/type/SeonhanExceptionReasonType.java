package com.seonhansite.server.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SeonhanExceptionReasonType {

    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
    NOT_FOUND_ANSWER(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),
    ALREADY_DELETED_QUESTION(HttpStatus.BAD_REQUEST, "질문이 이미 삭제되었습니다."),
    ALREADY_DELETED_ANSWER(HttpStatus.BAD_REQUEST,"답변이 이미 삭제되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "제목 / 내용 / 비밀번호 / 작성자를 입력해 주세요"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "Invalid argument"),
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
