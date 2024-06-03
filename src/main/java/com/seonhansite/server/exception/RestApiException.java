package com.seonhansite.server.exception;

import com.seonhansite.server.type.SeonhanExceptionReasonType;
import com.seonhansite.server.type.SeonhanMethodType;
import com.seonhansite.server.type.SeonhanResourceType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class RestApiException extends ResponseStatusException {
    private final SeonhanResourceType seonhanResourceType;
    private final SeonhanMethodType methodType;
    private final SeonhanExceptionReasonType reasonType;
    private final Long id;
    private final String validationMessage;

    public RestApiException(HttpStatus status,
                            @NotNull SeonhanMethodType method,
                            @NotNull SeonhanResourceType resource,
                            @NotNull SeonhanExceptionReasonType reason,
                            Long id) {

        super(status);
        this.seonhanResourceType = resource;
        this.methodType = method;
        this.reasonType = reason;
        this.id = id;
        this.validationMessage = null;
    }

    public RestApiException(HttpStatus status,
                            @NotNull SeonhanExceptionReasonType reason,
                            String message) {

        super(status);
        this.seonhanResourceType = null;
        this.methodType = null;
        this.reasonType = reason;
        this.id = null;
        this.validationMessage = message;
    }

    public String getDetailedMessage() {
        return this.validationMessage != null ? this.validationMessage : this.reasonType.getMessage();
    }

    public String getCustomMessage() {
        return this.reasonType.getMessage();
    }
}
