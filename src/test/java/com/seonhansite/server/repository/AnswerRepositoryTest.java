package com.seonhansite.server.repository;


import com.seonhansite.server.answer.Answer;
import com.seonhansite.server.exception.AnswerNotFoundException;
import com.seonhansite.server.answer.AnswerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @PersistenceContext
    private EntityManager em;

    private void clear() {
        em.flush();
        em.clear();
    }

    @Test
    void createAndReadTest() {
        // given
        Answer a = createAnswers();

        // when
        this.answerRepository.save(a);
        clear();

        //then
        Answer savedAnswer = this.answerRepository.findById(a.getId()).orElseThrow(AnswerNotFoundException::new);
        assertEquals(a.getId(), savedAnswer.getId());
    }

    @Test
    void questionDateTest() {
        Answer a = createAnswers();

        this.answerRepository.save(a);
        clear();

        Answer savedAnswer = this.answerRepository.findById(a.getId()).orElseThrow(AnswerNotFoundException::new);
        assertThat(savedAnswer.getCreatedAt()).isNotNull();
        assertThat(savedAnswer.getUpdatedAt()).isNotNull();
        assertThat(savedAnswer.getCreatedAt()).isEqualTo(savedAnswer.getUpdatedAt());
    }

    @Test
    @DisplayName("Content더티체킹")
    void updateContentTest() {
        String updatedContent = "updated Content";
        Answer a = this.answerRepository.save(createAnswers());
        clear();

        Answer savedAnswer = this.answerRepository.findById(a.getId()).orElseThrow(AnswerNotFoundException::new);
        savedAnswer.updateContent(updatedContent);
        clear();

        Answer updatedAnswer = this.answerRepository.findById(a.getId()).orElseThrow(AnswerNotFoundException::new);
        assertThat(updatedAnswer.getContent()).isEqualTo(updatedContent);
    }


    private Answer createAnswer(String author, String content) {
        return Answer.builder()
                .author(author)
                .content(content)
                .build();
    }

    private Answer createAnswers() {
        return Answer.builder()
                .author("jaeookk")
                .content("내용 입니다.")
                .build();
    }
}
