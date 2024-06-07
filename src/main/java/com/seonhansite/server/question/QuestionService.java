package com.seonhansite.server.question;


import com.seonhansite.server.answer.Answer;
import com.seonhansite.server.exception.RestApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seonhansite.server.type.SeonhanExceptionReasonType.*;
import static com.seonhansite.server.type.SeonhanMethodType.*;
import static com.seonhansite.server.type.SeonhanResourceType.QUESTION;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public QuestionListResponse getList(String author, String subject, String content, Integer page, Integer limit) {
        int offset = page * limit;

        List<QuestionResponse> pq = this.questionRepository.findAllByCustom(author, subject, content, limit, offset).stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());

        Integer totalElements = questionRepository.countByCustom(author, subject, content);

        QuestionListResponse response = new QuestionListResponse();

        response.setRows(pq);
        response.setCount(totalElements);
        response.setPage(page);
        response.setMessage("QnA 목록을 받아왔습니다.");
        return response;
    }

    @Transactional
    public QuestionResponse getQuestion(Long id) {
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, QUESTION, NOT_FOUND_QUESTION, id));
        return new QuestionResponse(question);
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        Question q = Question.builder()
                .author(request.getAuthor().trim())
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .subject(request.getSubject())
                .content(request.getContent())
                .build();
        this.questionRepository.save(q);
        return new QuestionResponse(q);
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionUpdateRequest request) {
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, UPDATE, QUESTION, NOT_FOUND_QUESTION, id));
        question.updateSubject(request.getSubject());
        question.updateContent(request.getContent());
        em.flush();
        return new QuestionResponse(question);
    }

    @Transactional
    public Integer deleteQuestion(Long id) {
        try {
            Question question = this.questionRepository.findById(id)
                    .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, QUESTION, NOT_FOUND_QUESTION, id));
            if (question.getIsDeleted() != null) {
                throw new RestApiException(NOT_FOUND, DELETE, QUESTION, ALREADY_DELETED_QUESTION, id);
            }
            this.questionRepository.delete(question);
            List<Answer> answers = question.getAnswerList();
            for (Answer answer : answers) {
                answer.updateIsDeleted();
            }
            return 1;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void checkQuestionPassword(Long id, String password) {
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, QUESTION, NOT_FOUND_QUESTION, id));

        if (!passwordEncoder.matches(password, question.getPassword())) {
            throw new RestApiException(UNAUTHORIZED, VALIDATION, QUESTION, INVALID_PASSWORD, id);
        }
    }
}
