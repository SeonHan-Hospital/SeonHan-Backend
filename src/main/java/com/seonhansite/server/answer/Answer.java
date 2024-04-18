package com.seonhansite.server.answer;

import com.seonhansite.server.common.entity.BaseEntity;
import com.seonhansite.server.question.Question;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE answer SET is_deleted = NOW() WHERE id = ?")
@SQLRestriction("is_deleted is null")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private LocalDateTime isDeleted;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateIsDeleted() {
        this.isDeleted = LocalDateTime.now();
    }
}
