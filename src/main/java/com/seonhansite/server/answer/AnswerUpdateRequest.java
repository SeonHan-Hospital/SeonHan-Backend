package com.seonhansite.server.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerUpdateRequest {
    @NotNull
    private Long questionId;
    @NotBlank
    private String author;
    @NotBlank
    private String content;
}
