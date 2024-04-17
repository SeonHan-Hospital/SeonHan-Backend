package com.seonhansite.server.answer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerListResponse {
    private List<AnswerResponse> data;
    private String message;
    private Integer count;
}
