package com.seonhansite.server.repository;


import com.seonhansite.server.answer.Answer;
import com.seonhansite.server.question.Question;
import com.seonhansite.server.exception.AnswerNotFoundException;
import com.seonhansite.server.exception.QuestionNotFoundException;
import com.seonhansite.server.answer.AnswerRepository;
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


@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
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
        Question q = createQuestions();

        // when
        this.questionRepository.save(q);
        clear();

        //then
        Question savedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        assertEquals(q.getId(), savedQuestion.getId());
    }

    @Test
    void questionDateTest() {
        Question q = createQuestions();

        this.questionRepository.save(q);
        clear();

        Question savedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        assertThat(savedQuestion.getCreatedAt()).isNotNull();
        assertThat(savedQuestion.getUpdatedAt()).isNotNull();
        assertThat(savedQuestion.getCreatedAt()).isEqualTo(savedQuestion.getUpdatedAt());
    }

    @Test
    @DisplayName("Content더티체킹")
    void updateContentTest() {
        String updatedContent = "updated Content";
        Question q = this.questionRepository.save(createQuestions());
        clear();

        Question savedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        savedQuestion.updateContent(updatedContent);
        clear();

        Question updatedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        assertThat(updatedQuestion.getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("Subject더티체킹")
    void updateSubjectTest() {
        String updatedSubject = "updated Subject";
        Question q = this.questionRepository.save(createQuestions());
        clear();

        Question savedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        savedQuestion.updateContent(updatedSubject);
        clear();

        Question updatedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        assertThat(updatedQuestion.getContent()).isEqualTo(updatedSubject);
    }


    @Test
    void findBySubjectTest() {
        Question q = createQuestion("J", "qwer", "제목을 입력해주세요.", "내용");
        this.questionRepository.save(q);
        clear();

        Question savedQuestion = this.questionRepository.findBySubject(q.getSubject()).orElseThrow(QuestionNotFoundException::new);

        assertThat(savedQuestion.getId()).isEqualTo(q.getId());
    }

    @Test
    void findByAuthorTest() {
        Question q = createQuestion("J", "qwer", "제목을 입력해주세요.", "내용");
        this.questionRepository.save(q);
        clear();

        Question savedQuestion = this.questionRepository.findByAuthor(q.getAuthor()).orElseThrow(QuestionNotFoundException::new);

        assertThat(savedQuestion.getId()).isEqualTo(q.getId());
    }


    @Test
    void findBySubjectLikeTest() {
        String kw = "제목";
        Question q1 = createQuestion("J", "qwer", "제목을 입력해주세요.", "내용");
        Question q2 = createQuestion("J", "qwer", "제목 입니다.", "내용");
        this.questionRepository.save(q1);
        clear();
        this.questionRepository.save(q2);
        clear();

        List<Question> savedQuestionList = this.questionRepository.findBySubjectLike("%"+kw+"%");

        assertThat(savedQuestionList.size()).isEqualTo(2);
    }

    @Test
    void findByAuthorLikeTest() {
        String kw = "author";
        Question q1 = createQuestion("author", "qwer", "제목을 입력해주세요.", "내용");
        Question q2 = createQuestion("author123", "qwer", "제목 입니다.", "내용");
        this.questionRepository.save(q1);
        clear();
        this.questionRepository.save(q2);
        clear();

        List<Question> savedQuestionList = this.questionRepository.findByAuthorLike("%"+kw+"%");

        assertThat(savedQuestionList.size()).isEqualTo(2);
    }

    @Test
    void getQuestionFromAnswerTest() {
        Question q = createQuestions();
        this.questionRepository.save(q);
        clear();

        Answer a = Answer.builder()
                .author("test")
                .content("답변 내용 입니다.")
                .question(q)
                .build();
        this.answerRepository.save(a);
        clear();

        Answer sa = this.answerRepository.findById(a.getId()).orElseThrow(AnswerNotFoundException::new);

        assertThat(sa.getQuestion().getId()).isEqualTo(q.getId());
        assertThat(sa.getQuestion().getSubject()).isEqualTo("제목 입니다.");
    }

    @Test
    void getAnswerListFromQuestion() {
        Question q = createQuestions();
        this.questionRepository.save(q);
        clear();

        Answer a = Answer.builder()
                .author("test")
                .content("답변 내용 입니다.")
                .question(q)
                .build();
        this.answerRepository.save(a);
        clear();

        Question savedQuestion = this.questionRepository.findById(q.getId()).orElseThrow(QuestionNotFoundException::new);
        List<Answer> answerList = savedQuestion.getAnswerList();

        assertThat(answerList.size()).isEqualTo(1);
        assertThat(answerList.get(0).getContent()).isEqualTo(a.getContent());
    }

    private Question createQuestion(String author, String password, String subject, String content) {
        return Question.builder()
                .author(author)
                .password(password)
                .subject(subject)
                .content(content)
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
