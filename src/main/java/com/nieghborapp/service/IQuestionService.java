package com.nieghborapp.service;

import com.nieghborapp.domain.Question;
import com.nieghborapp.exceptions.NotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IQuestionService {

    void createQuestion(Authentication authentication, Question question);
    void deleteQuestion(Authentication authentication,Long id) throws Exception;
    Question getQuetion(Authentication authentication,Long id) throws NotFoundException;

    Question updateQuestion(Authentication authentication, Long id, Question question) throws Exception  ;
    void addToFavorite(Authentication authentication,Long postId) throws NotFoundException;
    List<Question> getFavoriteByUser(Authentication authentication) throws NotFoundException;
    void deleteFavorite(Authentication authentication,Long id) throws NotFoundException;
}
