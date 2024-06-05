package com.seonhansite.server.question;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {
    @NotBlank
    private String author;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
}
