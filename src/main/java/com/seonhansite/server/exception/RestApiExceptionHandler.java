package com.seonhansite.server.exception;


import com.seonhansite.server.type.SeonhanExceptionReasonType;
import com.seonhansite.server.type.SeonhanMethodType;
import com.seonhansite.server.type.SeonhanResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<RestApiExceptionMessage> handleCustomException(RestApiException exception) {
        return new ResponseEntity<>(
                new RestApiExceptionMessage(exception),
                exception.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        FieldError fieldError = fieldErrors.get(fieldErrors.size()-1);  // 가장 첫 번째 에러 필드
        String fieldName = fieldError.getField();   // 필드명
        Object rejectedValue = fieldError.getRejectedValue();   // 입력값
//        String message = fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.";
        String message = fieldName + "를 작성해주세요.";


        RestApiException validationException = new RestApiException(
                HttpStatus.BAD_REQUEST,
                SeonhanExceptionReasonType.INVALID_REQUEST,
                message
        );

        return new ResponseEntity<>(
                new RestApiExceptionMessage(validationException),
                validationException.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestApiExceptionMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("handleIllegalArgumentException", ex);

        RestApiException customException = new RestApiException(
                HttpStatus.BAD_REQUEST,
                SeonhanMethodType.UNKNOWN,
                SeonhanResourceType.UNKNOWN,
                SeonhanExceptionReasonType.INVALID_ARGUMENT,
                null
        );

        return new ResponseEntity<>(
                new RestApiExceptionMessage(customException),
                customException.getStatusCode()
        );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<RestApiExceptionMessage> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);

        RestApiException customException = new RestApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                SeonhanMethodType.UNKNOWN,
                SeonhanResourceType.UNKNOWN,
                SeonhanExceptionReasonType.GENERAL_ERROR,
                null
        );

        return new ResponseEntity<>(
                new RestApiExceptionMessage(customException),
                customException.getStatusCode()
        );
    }
}
