package com.seonhansite.server.question;

import com.seonhansite.server.common.entity.BaseEntity;
import com.seonhansite.server.answer.Answer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE question SET is_deleted = NOW() WHERE id = ?")
@SQLRestriction("is_deleted is null") // 조회 시 isDeleted가 null이 아닌 레코드만 반환
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

    private LocalDateTime isDeleted;

    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Answer> answerList = new ArrayList<>();

    public void updateSubject(String subject) {
        this.subject = subject;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
