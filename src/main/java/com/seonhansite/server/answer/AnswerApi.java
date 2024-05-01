package com.seonhansite.server.answer;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
@CrossOrigin(origins = "*")
public class AnswerApi {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponse> createAnswer(@Valid @RequestBody AnswerCreateRequest answerCreateRequest) {
        AnswerResponse answerResponse = answerService.createAnswer(answerCreateRequest);
        answerResponse.setMessage("답변 작성이 완료되었습니다.");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(answerResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(answerResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> answerDetail(@PathVariable("id") Long id) {
        AnswerResponse answerResponse = this.answerService.getAnswer(id);
        answerResponse.setMessage("답변 상세목록을 받아왔습니다.");
        return ResponseEntity.ok().body(answerResponse);
    }

    @GetMapping
    public ResponseEntity<AnswerListResponse> answerList(@RequestParam(value = "questionId") Long questionId) {
        AnswerListResponse response = this.answerService.getList(questionId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(@PathVariable("id") Long id, @Valid @RequestBody AnswerUpdateRequest request) {
        AnswerResponse answerResponse = this.answerService.updateAnswer(id, request);
        answerResponse.setMessage("답변 수정이 완료되었습니다.");
        return ResponseEntity.ok().body(answerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable("id") Long id) throws UnsupportedEncodingException {
        Integer res = this.answerService.deleteAnswer(id);
        return res == 1 ? new ResponseEntity<>(new String("답변 삭제 성공".getBytes(), "utf-8"), HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
