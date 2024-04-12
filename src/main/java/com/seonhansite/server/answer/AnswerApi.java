package com.seonhansite.server.answer;


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
@RequestMapping("/answer")
@CrossOrigin(origins = "*")
public class AnswerApi {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponse> createAnswer(@Valid @RequestBody AnswerCreateRequest answerCreateRequest) {
        AnswerResponse answerResponse = answerService.createAnswer(answerCreateRequest);
        return ResponseEntity.ok().body(answerResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> answerDetail(@PathVariable("id") Long id) {
        AnswerResponse answerResponse = this.answerService.getAnswer(id);
        return ResponseEntity.ok().body(answerResponse);
    }

    @GetMapping
    public ResponseEntity<AnswerListResponse> answerList(
            @RequestParam(value = "questionId") Long questionId,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit) {

        AnswerListResponse response = this.answerService.getList(questionId, page, limit);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(@PathVariable("id") Long id, @Valid @RequestBody AnswerUpdateRequest request) {
        AnswerResponse answerResponse = this.answerService.updateAnswer(id, request);
        return ResponseEntity.ok().body(answerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable("id") Long id) throws UnsupportedEncodingException {
        Integer res = this.answerService.deleteAnswer(id);
        return res == 1 ? new ResponseEntity<>(new String("답변 삭제 성공".getBytes(), "utf-8"), HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
