package com.seonhansite.server.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
@CrossOrigin(origins = "*")
public class QuestionApi {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<QuestionListResponse> questionList(
            @RequestParam(value = "author", defaultValue = "") String author,
            @RequestParam(value = "subject", defaultValue = "") String subject,
            @RequestParam(value = "content", defaultValue = "") String content,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        QuestionListResponse response = this.questionService.getList(author, subject, content, page, limit);

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> questionDetail(@PathVariable("id") Long id) {
        QuestionResponse questionResponse = this.questionService.getQuestion(id);
        questionResponse.setMessage("QnA 상세목록을 받아왔습니다.");
        return ResponseEntity.ok().body(questionResponse);
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionCreateRequest request) {
        QuestionResponse questionResponse = this.questionService.createQuestion(request);
        questionResponse.setMessage("QnA 작성이 완료되었습니다.");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(questionResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(questionResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionResponse questionResponse = this.questionService.updateQuestion(id, request);
        questionResponse.setMessage("QnA 수정이 완료되었습니다.");
        return ResponseEntity.ok().body(questionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("id") Long id) throws UnsupportedEncodingException {
        Integer res = this.questionService.deleteQuestion(id);
        return res == 1 ? new ResponseEntity<>(new String("질문 삭제 성공".getBytes(), "utf-8"), HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> questionDetailByPassword(@PathVariable("id") Long id, @RequestBody Map<String, String> requestBody) {
        String password = requestBody.get("password");
        questionService.checkQuestionPassword(id, password);
        return ResponseEntity.noContent().build();
    }
}
