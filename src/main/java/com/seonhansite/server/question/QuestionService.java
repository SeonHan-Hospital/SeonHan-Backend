package com.seonhansite.server.question;


import com.seonhansite.server.exception.DataNotFoundException;
import com.seonhansite.server.exception.QuestionNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    @PersistenceContext
    private final EntityManager em;

//    @Transactional
//    public QuestionListResponse getList(String author, String subject, String content, Integer page, Integer limit) {
//        // TODO : Implement filtering, pagination
//
//        List<QuestionResponse> ql = this.questionRepository.findAll().stream().map(QuestionResponse::new).sorted(Comparator.comparing(QuestionResponse::getCreatedAt).reversed()).toList();
//
//        int count = ql.size();
//        int pageNumber = (page != null) ? page : 1;
//
//        QuestionListResponse response = new QuestionListResponse();
//        response.setRows(ql);
//        response.setCount(count);
//        response.setPage(pageNumber);
//        return response;
//    }

    @Transactional
    public QuestionListResponse getList(String author, String subject, String content, Integer page, Integer limit) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sorts));
        Page<QuestionResponse> pq = this.questionRepository.findAll(pageable).map(QuestionResponse::new);

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
