package com.seonhansite.server.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {
    @NotBlank
    private String author;
    @NotBlank
    private String password;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
}
