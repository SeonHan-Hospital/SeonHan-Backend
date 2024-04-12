package com.seonhansite.server.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
@CrossOrigin(origins = "*")
public class QuestionApi {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<QuestionListResponse> questionList(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) {

        QuestionListResponse response = this.questionService.getList(author, subject, content, page, limit);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> questionDetail(@PathVariable("id") Long id) {
        QuestionResponse questionResponse = this.questionService.getQuestion(id);
        return ResponseEntity.ok().body(questionResponse);
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionCreateRequest request) {
        QuestionResponse questionResponse = this.questionService.createQuestion(request);
        return ResponseEntity.ok().body(questionResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionResponse questionResponse = this.questionService.updateQuestion(id, request);
        return ResponseEntity.ok().body(questionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("id") Long id) throws UnsupportedEncodingException {
        Integer res = this.questionService.deleteQuestion(id);
        return res == 1 ? new ResponseEntity<>(new String("질문 삭제 성공".getBytes(), "utf-8"), HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
