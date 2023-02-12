package com.nieghborapp.web;

import com.nieghborapp.domain.Question;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IQuestionRepository;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController @Slf4j @RequestMapping("api/questions") @RequiredArgsConstructor
public class QuetionsController {

    private final IQuestionService questioService;
//    private final Authentication authentication;
    private final IUserRepository userRepository;
    private final IQuestionRepository questionRepository;

    @PostMapping("/")
    void createPost(Authentication authentication,@Valid @RequestBody Question questionDto){
        questioService.createQuestion(authentication,questionDto);
    }


    @DeleteMapping("/{id}")
    void deleteQuestion (Authentication authentication,@PathVariable Long id) throws Exception {
        log.info("deleting a question ");
        questioService.deleteQuestion(authentication,  id);
    }

    @GetMapping("/{id}")
    ResponseEntity<Question> getQuestion (Authentication authentication,@PathVariable Long id) throws NotFoundException {
        log.info("geting  a question ");
        return  new ResponseEntity(questioService.getQuetion(authentication,  id),null,HttpStatus.ACCEPTED);
    }

    @PutMapping("{id}")
    ResponseEntity<Question> modifyQuestion (Authentication authentication,@PathVariable Long id,@RequestBody Question question) throws Exception {
        log.info("updating   a question ");
        return  new ResponseEntity(questioService.updateQuestion(authentication,  id,question),null,HttpStatus.ACCEPTED);
    }

    @PostMapping("/favorite/{postId}")
    void addToFavorite (Authentication authentication,@PathVariable Long postId) throws NotFoundException {
        log.info("adding post to favorite ");
        questioService.addToFavorite(authentication,postId);

    }

    @GetMapping("/favorite")
    ResponseEntity<List<Question>> getFavoriteByUser(Authentication authentication) throws NotFoundException {
       return new ResponseEntity<>(questioService.getFavoriteByUser(authentication),null,HttpStatus.ACCEPTED);
    }


    @GetMapping("/user")
    ResponseEntity<List<Question>> getUserQuestions(Authentication authentication){
        log.info("geting the use's question ");
        return new ResponseEntity<>(questionRepository.findAllByUser(
                userRepository.findByUsername(authentication.getName()).orElseThrow()
        ).orElseThrow(),null,HttpStatus.ACCEPTED) ;
    }




    @GetMapping("/user/all")
    ResponseEntity<List<Question>> getAllQuestions(Authentication authentication){
        log.info("geting the use's question ");
        return new ResponseEntity<>(questionRepository.findAll(),null,HttpStatus.ACCEPTED) ;
    }




}
