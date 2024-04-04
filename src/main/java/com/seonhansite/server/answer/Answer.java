package com.seonhansite.server.answer;

import com.seonhansite.server.common.entity.BaseEntity;
import com.seonhansite.server.question.Question;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    public void updateContent(String content) {
        this.content = content;
    }
}
