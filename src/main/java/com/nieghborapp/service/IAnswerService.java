package com.nieghborapp.service;

import com.nieghborapp.domain.Answer;
import com.nieghborapp.exceptions.NotFoundException;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IAnswerService {

     Answer addAnswer(Authentication authentication, Answer answer,Long QuestionId) throws NotFoundException;
     void deleteAnswer(Authentication authentication,Long answerId ) throws Exception;
     Answer modifyAnswer(Authentication authentication, Answer answer,Long answerId) throws Exception;
     List<Answer> getAnswersByQuestion(Authentication authentication, Long QuestionId) throws Exception;

}
