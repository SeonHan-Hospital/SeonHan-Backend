package com.seonhansite.server.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeonhanExceptionReasonType {

    NOT_FOUND_QUESTION("Question not found with id"),
    ALREADY_DELETED_QUESTION("Question is already deleted with id"),
    NOT_FOUND_ANSWER("Answer not found with id"),
    ALREADY_DELETED_ANSWER("Answer is already deleted with id"),
    INVALID_PASSWORD("Invalid password with id")
    ;

    final String message;

    public String getMessageWithId(Long id) {
        return this.message + ": " + id;
    }
}
