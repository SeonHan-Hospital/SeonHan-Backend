package com.seonhansite.server.answer;


import com.seonhansite.server.exception.AnswerNotFoundException;
import com.seonhansite.server.exception.DataNotFoundException;
import com.seonhansite.server.question.Question;
import com.seonhansite.server.question.QuestionRepository;
import com.seonhansite.server.question.QuestionResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return new AnswerResponse(answer.get());
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    @Transactional
    public AnswerListResponse getList(Long id, Integer page, Integer limit) {
        List<AnswerResponse> al = this.answerRepository.findAllByQuestionId(id).stream().map(AnswerResponse::new).sorted(Comparator.comparing(AnswerResponse::getCreatedAt).reversed()).toList();

        int count = al.size();
        int pageNumber = (page != null) ? page : 1;

        AnswerListResponse response = new AnswerListResponse();
        response.setData(al);
        response.setCount(count);
        response.setPage(pageNumber);

        return response;
    }


    @Transactional
    public AnswerResponse createAnswer(AnswerCreateRequest answerCreateRequest) {
        Optional<Question> question = this.questionRepository.findById(answerCreateRequest.getQuestionId());
        if (question.isPresent()) {
            Answer answer = Answer.builder()
                    .author(answerCreateRequest.getAuthor())
                    .content(answerCreateRequest.getContent())
                    .question(question.get())
                    .build();
            this.answerRepository.save(answer);
            return new AnswerResponse(answer);
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    @Transactional
    public AnswerResponse updateAnswer(Long id, AnswerUpdateRequest request) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            Answer a = answer.get();
            a.updateContent(request.getContent());
            em.flush();
            return new AnswerResponse(a);
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    @Transactional
    public Integer deleteAnswer(Long id) {
        try {
            Answer answer = this.answerRepository.findById(id).orElseThrow(AnswerNotFoundException::new);
            this.answerRepository.delete(answer);
            return 1;
        } catch (AnswerNotFoundException e) {
            return 0;
        }
    }
}
