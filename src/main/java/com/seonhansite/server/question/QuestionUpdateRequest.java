package com.seonhansite.server.question;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {
    @NotNull
    private String author;
    @NotNull
    private String subject;
    @NotNull
    private String content;
}
