package com.seonhansite.server.answer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequest {
    @NotNull
    private Long questionId;
    @NotNull
    private String author;
    @NotNull
    private String content;
}
