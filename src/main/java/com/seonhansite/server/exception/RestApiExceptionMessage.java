package com.seonhansite.server.exception;

import com.seonhansite.server.type.SeonhanMethodType;
import com.seonhansite.server.type.SeonhanResourceType;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class RestApiExceptionMessage {
    private final HttpStatusCode code;
    private final SeonhanResourceType resource;
    private final SeonhanMethodType method;
    private final String reason;

    public RestApiExceptionMessage(RestApiException exception) {
        this.code = exception.getStatusCode();
        this.resource = exception.getSeonhanResourceType();
        this.method = exception.getMethodType();
        this.reason = exception.getReasonType().getMessageWithId(exception.getId());
    }
}
