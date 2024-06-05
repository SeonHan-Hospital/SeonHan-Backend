package com.seonhansite.server.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionListResponse {
    private List<QuestionResponse> rows;
    private String message;
    private Integer count;
    private Integer page;
}
