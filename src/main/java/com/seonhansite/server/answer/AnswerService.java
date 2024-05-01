package com.seonhansite.server.answer;


import com.seonhansite.server.exception.RestApiException;
import com.seonhansite.server.question.Question;
import com.seonhansite.server.question.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.seonhansite.server.type.SeonhanExceptionReasonType.*;
import static com.seonhansite.server.type.SeonhanMethodType.*;
import static com.seonhansite.server.type.SeonhanResourceType.ANSWER;
import static com.seonhansite.server.type.SeonhanResourceType.QUESTION;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class AnswerService {

    @Autowired
    private final AnswerRepository answerRepository;
    @Autowired
    private final QuestionRepository questionRepository;
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public AnswerResponse getAnswer(Long id) {
        Answer answer = this.answerRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, ANSWER, NOT_FOUND_ANSWER, id));
        return new AnswerResponse(answer);
    }

    @Transactional
    public AnswerListResponse getList(Long id) {
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, QUESTION, NOT_FOUND_QUESTION, id));

        List<AnswerResponse> al = this.answerRepository.findAllByQuestionId(id).stream().map(AnswerResponse::new).sorted(Comparator.comparing(AnswerResponse::getCreatedAt).reversed()).toList();

        int count = al.size();

        AnswerListResponse response = new AnswerListResponse();
        response.setData(al);
        response.setCount(count);
        response.setMessage("답변 목록을 받아왔습니다.");

        return response;
    }


    @Transactional
    public AnswerResponse createAnswer(AnswerCreateRequest answerCreateRequest) {
        Long id = answerCreateRequest.getQuestionId();
        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, QUESTION, NOT_FOUND_QUESTION, id));
        Answer answer = Answer.builder()
                .author(answerCreateRequest.getAuthor())
                .content(answerCreateRequest.getContent())
                .question(question)
                .build();
        this.answerRepository.save(answer);
        return new AnswerResponse(answer);
    }

    @Transactional
    public AnswerResponse updateAnswer(Long id, AnswerUpdateRequest request) {
        Answer answer = this.answerRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NOT_FOUND, UPDATE, ANSWER, NOT_FOUND_ANSWER, id));

        answer.updateContent(request.getContent());
        em.flush();
        return new AnswerResponse(answer);
    }

    @Transactional
    public Integer deleteAnswer(Long id) {
        try {
            Answer answer = this.answerRepository.findById(id)
                    .orElseThrow(() -> new RestApiException(NOT_FOUND, READ, ANSWER, NOT_FOUND_ANSWER, id));
            if (answer.getIsDeleted() != null) {
                throw new RestApiException(NOT_FOUND, DELETE, ANSWER, ALREADY_DELETED_ANSWER, id);
            }
            this.answerRepository.delete(answer);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
