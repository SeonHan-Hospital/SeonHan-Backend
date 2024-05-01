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
    }
}
