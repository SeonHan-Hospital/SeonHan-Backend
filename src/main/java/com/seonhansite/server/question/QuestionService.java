package com.seonhansite.server.question;


import com.seonhansite.server.exception.DataNotFoundException;
import com.seonhansite.server.exception.QuestionNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public List<QuestionResponse> getList() {
        List<Question> questionList = this.questionRepository.findAll();
        return questionList.stream().map(QuestionResponse::new).toList();
    }

    @Transactional
    public QuestionResponse getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return new QuestionResponse(question.get());
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        Question q = Question.builder()
                .author(request.getAuthor())
                .password(request.getPassword())
                .subject(request.getSubject())
                .content(request.getContent())
                .build();
        this.questionRepository.save(q);
        return new QuestionResponse(q);
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionUpdateRequest request) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            Question q = question.get();
            q.updateSubject(request.getSubject());
            q.updateContent(request.getContent());
            em.flush();
            return new QuestionResponse(q);
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    @Transactional
    public Integer deleteQuestion(Long id) {
        try {
            Question q = this.questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
            this.questionRepository.delete(q);
            return 1;
        } catch (QuestionNotFoundException e) {
            return 0;
        }
    }
}
