package com.seonhansite.server.question;

import com.seonhansite.server.common.entity.BaseEntity;
import com.seonhansite.server.answer.Answer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    private String password;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    public void updateSubject(String subject) {
        this.subject = subject;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
