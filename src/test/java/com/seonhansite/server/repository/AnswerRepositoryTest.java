package com.seonhansite.server.repository;


import com.seonhansite.server.answer.Answer;
import com.seonhansite.server.exception.AnswerNotFoundException;
import com.seonhansite.server.answer.AnswerRepository;
import com.seonhansite.server.question.Question;
import com.seonhansite.server.question.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

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
    void answerDateTest() {
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

    @Test
    void findAllByQuestionIdTest() {
        Question q = createQuestions();
        Answer a = createAnswer("test", "test content", q);
        Answer b = createAnswer("test2", "test content2", q);

        this.questionRepository.save(q);
        clear();
        this.answerRepository.save(a);
        clear();
        this.answerRepository.save(b);
        clear();

        List<Answer> savedAnswer = this.answerRepository.findAllByQuestionId(q.getId());

        assertThat(savedAnswer.size()).isEqualTo(2);
        assertThat(savedAnswer.get(0).getId()).isEqualTo(a.getId());
        assertThat(savedAnswer.get(0).getAuthor()).isEqualTo(a.getAuthor());
        assertThat(savedAnswer.get(0).getContent()).isEqualTo(a.getContent());
        assertThat(savedAnswer.get(1).getId()).isEqualTo(b.getId());
        assertThat(savedAnswer.get(1).getAuthor()).isEqualTo(b.getAuthor());
        assertThat(savedAnswer.get(1).getContent()).isEqualTo(b.getContent());

    }

    private Answer createAnswer(String author, String content, Question q) {
        return Answer.builder()
                .author(author)
                .content(content)
                .question(q)
                .build();
    }

    private Answer createAnswers() {
        return Answer.builder()
                .author("jaeookk")
                .content("내용 입니다.")
                .build();
    }

    private Question createQuestions() {
        return Question.builder()
                .author("jaeookk")
                .password("qwer1234")
                .subject("제목 입니다.")
                .content("내용 입니다.")
                .build();
    }
}
