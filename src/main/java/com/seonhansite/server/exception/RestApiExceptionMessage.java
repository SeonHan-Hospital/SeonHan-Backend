package com.seonhansite.server.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seonhansite.server.type.SeonhanExceptionReasonType;
import com.seonhansite.server.type.SeonhanMethodType;
import com.seonhansite.server.type.SeonhanResourceType;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;


@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestApiExceptionMessage {
    private final HttpStatusCode code;
    private final SeonhanResourceType resource;
    private final SeonhanMethodType method;
    private final SeonhanExceptionReasonType reasonType;
    private final Long id;
    private final String message;

    public RestApiExceptionMessage(RestApiException exception) {
        this.code = exception.getStatusCode();
        this.resource = exception.getSeonhanResourceType();
        this.method = exception.getMethodType();
        this.reasonType = exception.getReasonType();
        this.id = exception.getId();
        this.message = exception.getDetailedMessage();
    }
}
