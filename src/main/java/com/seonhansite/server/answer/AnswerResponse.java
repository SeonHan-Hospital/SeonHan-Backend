package com.seonhansite.server.answer;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerResponse {
    private Long id;
    private String author;
    private String content;
    private Long questionId;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AnswerResponse(Answer answer) {
        this.id = answer.getId();
        this.author = answer.getAuthor();
        this.content = answer.getContent();
        this.questionId = answer.getQuestion().getId();
        this.createdAt = answer.getCreatedAt();
        this.updatedAt = answer.getUpdatedAt();
    }
}