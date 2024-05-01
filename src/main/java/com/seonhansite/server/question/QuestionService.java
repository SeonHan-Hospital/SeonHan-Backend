package com.seonhansite.server.question;


import com.seonhansite.server.answer.Answer;
import com.seonhansite.server.exception.RestApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.seonhansite.server.type.SeonhanExceptionReasonType.*;
import static com.seonhansite.server.type.SeonhanMethodType.*;
import static com.seonhansite.server.type.SeonhanResourceType.QUESTION;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@RequiredArgsConstructor
@Service
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public QuestionListResponse getList(String author, String subject, String content, Integer page, Integer limit) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sorts));

        Specification<Question> spec = Specification.where(
                QuestionSpecifications.hasAuthor(author)
                        .and(QuestionSpecifications.hasSubject(subject))
                        .and(QuestionSpecifications.hasContent(content))
        );

        Page<QuestionResponse> pq = this.questionRepository.findAll(spec, pageable).map(QuestionResponse::new);

        Integer count = Math.toIntExact(pq.getTotalElements());


        QuestionListResponse response = new QuestionListResponse();
        response.setRows(pq.getContent());
        response.setCount(count);
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
                .author(request.getAuthor())
                .password(passwordEncoder.encode(request.getPassword()))
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
            return 0;
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

    private static class QuestionSpecifications {
        public static Specification<Question> hasAuthor(String author) {
            return (root, query, criteriaBuilder) ->
                    author != null && !author.isEmpty() ? criteriaBuilder.like(root.get("author"), "%" + author + "%") : null;
        }

        public static Specification<Question> hasSubject(String subject) {
            return (root, query, criteriaBuilder) ->
                    subject != null && !subject.isEmpty() ? criteriaBuilder.like(root.get("subject"), "%" + subject + "%") : null;
        }

        public static Specification<Question> hasContent(String content) {
            return (root, query, criteriaBuilder) ->
                    content != null && !content.isEmpty() ? criteriaBuilder.like(root.get("content"), "%" + content + "%") : null;
        }
    }

}
