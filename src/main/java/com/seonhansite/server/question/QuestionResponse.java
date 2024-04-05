package com.seonhansite.server.question;

import com.seonhansite.server.answer.Answer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private Long id;
    private String author;
    private String password;
    private String subject;
    private String content;
    private List<Long> answerId = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuestionResponse(Question question) {
        this.id = question.getId();
        this.author = question.getAuthor();
        this.password = question.getPassword();
        this.subject = question.getSubject();
        this.content = question.getContent();
        this.answerId = question.getAnswerList().stream()
                .map(Answer::getId)
                .toList();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
    }
}
