package com.nieghborapp.service.impl;

import com.nieghborapp.domain.Answer;
import com.nieghborapp.domain.Question;
import com.nieghborapp.domain.User;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IAnswerRepository;
import com.nieghborapp.repository.IQuestionRepository;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service   @RequiredArgsConstructor          @Slf4j
public class AnswerService implements IAnswerService {
    private final IAnswerRepository answerRepository;
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;

    @Override
    public Answer addAnswer(Authentication authentication, Answer answer,Long postId) throws NotFoundException {
        User loggedUser =     userRepository.findByUsername(authentication.getName()).orElseThrow(()->new NotFoundException("something is worng "));
        Question questionToAddAnswer  =     questionRepository.findById(postId).orElseThrow(()->new NotFoundException("the question was not found  "));
        answer.setQuestionId(questionToAddAnswer);
        answer.setUserId(loggedUser);
       return answerRepository.save(answer);


    }

    @Override
    public void deleteAnswer(Authentication authentication, Long answerId) throws Exception {
        //TODO PUT THIS LOGGED USER IN THE SERVICE OF THE USER
        User loggedUser =     userRepository.findByUsername(authentication.getName()).orElseThrow(()->new NotFoundException("something is worng "));
        Answer answerToDelete = answerRepository.findById(answerId).orElseThrow(()->new NotFoundException("the answer  was not found  "));

        if (loggedUser.getId().equals(answerToDelete.getUserId().getId())){
            answerRepository.deleteById(answerId);
            return;
        }
        log.error("there is an error you don't have permission ");
        throw new Exception("you don't have permission ");

    }

    @Override
    public Answer modifyAnswer(Authentication authentication, Answer answer, Long answerId) throws Exception {
        User loggedUser =     userRepository.findByUsername(authentication.getName()).orElseThrow(()->new NotFoundException("something is worng "));
        Answer answerToModify = answerRepository.findById(answerId).orElseThrow(()->new NotFoundException("the answer  was not found  "));
        if(loggedUser.getId().equals(answerToModify.getUserId().getId())){
            //TODO SET THE UPDATED TIME
            answerToModify.setContent(answer.getContent());
            return answerRepository.save(answerToModify);
        }
        log.error("there is an error you don't have permission ");
        throw new Exception("you don't have permission ");

    }

    @Override
    public List<Answer> getAnswersByQuestion(Authentication authentication, Long questionId) throws Exception {
        Question  questionToFetch = questionRepository.findById(questionId).orElseThrow(()->new NotFoundException("the question was not found "));
        return answerRepository.findAllByQuestionId(questionToFetch);
    }
}
