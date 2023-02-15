package com.nieghborapp.web;

import com.nieghborapp.domain.Answer;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.service.IAnswerService;
import com.nieghborapp.service.impl.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController() @RequestMapping("api/answers")    @RequiredArgsConstructor @Slf4j
public class AnswerController {
    private final IAnswerService answerService;
    @PostMapping("/{id}")
    public ResponseEntity<Answer> addAnswer(@RequestBody Answer answer, Authentication authentication, @PathVariable Long id) throws NotFoundException {
        log.info("ading answer ");
       return new ResponseEntity<>( answerService.addAnswer(authentication,answer,id),null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,Authentication authentication) throws Exception {
        log.info("removing the answer ");
         answerService.deleteAnswer(authentication,id);
    }
    @PutMapping("/{id}")
    public  ResponseEntity<Answer>  updateAnswer(@RequestBody Answer answer,@PathVariable Long id,Authentication authentication) throws Exception {
        log.info("updating  the answer ");
        return new ResponseEntity<>(answerService.modifyAnswer(authentication,answer,id),null,HttpStatus.ACCEPTED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<Answer>> getByQuestion(@PathVariable Long id,Authentication authentication) throws Exception {
        log.info("get answer by question ");
        return new ResponseEntity<>(answerService.getAnswersByQuestion(authentication,id),null,HttpStatus.ACCEPTED);

    }
}
